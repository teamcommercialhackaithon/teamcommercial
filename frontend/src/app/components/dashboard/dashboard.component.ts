import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DashboardService } from '../../services/dashboard.service';
import { DashboardStats } from '../../models/dashboard.model';
import { Chart, ChartConfiguration, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, AfterViewInit {
  @ViewChild('eventsChart') eventsChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('notificationsChart') notificationsChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('eventTypeChart') eventTypeChartRef!: ElementRef<HTMLCanvasElement>;

  dashboardStats: DashboardStats | null = null;
  loading = false;
  error = '';

  // Filter options
  selectedPeriod = 'today';
  periods = [
    { value: 'today', label: 'Today' },
    { value: 'week', label: 'This Week' },
    { value: 'month', label: 'This Month' },
    { value: 'three_months', label: 'Last 3 Months' }
  ];

  selectedGraphType = 'bar';
  graphTypes = [
    { value: 'bar', label: 'Bar Chart' },
    { value: 'line', label: 'Line Chart' },
    { value: 'pie', label: 'Pie Chart' },
    { value: 'doughnut', label: 'Doughnut Chart' }
  ];

  // Chart instances
  private eventsChart: Chart | null = null;
  private notificationsChart: Chart | null = null;
  private eventTypeChart: Chart | null = null;

  // Separate charts for each event type
  eventTypes: string[] = [];
  eventTypeCharts: Map<string, Chart> = new Map();

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  ngAfterViewInit(): void {
    // Charts will be created after data is loaded
  }

  loadDashboardData(): void {
    this.loading = true;
    this.error = '';

    this.dashboardService.getDashboardStats(this.selectedPeriod).subscribe({
      next: (data) => {
        this.dashboardStats = data;
        this.eventTypes = Object.keys(data.eventsByType);
        this.loading = false;
        
        // Create charts after a short delay to ensure DOM is ready
        setTimeout(() => {
          this.createCharts();
        }, 100);
      },
      error: (err) => {
        this.error = 'Failed to load dashboard data. Please check if the backend is running.';
        console.error('Error loading dashboard:', err);
        this.loading = false;
      }
    });
  }

  onPeriodChange(): void {
    this.destroyCharts();
    this.loadDashboardData();
  }

  onGraphTypeChange(): void {
    this.destroyCharts();
    setTimeout(() => {
      this.createCharts();
    }, 100);
  }

  private createCharts(): void {
    if (!this.dashboardStats) return;

    this.createEventsChart();
    this.createNotificationsChart();
    this.createEventTypeChart();
    this.createIndividualEventTypeCharts();
  }

  private createEventsChart(): void {
    if (!this.eventsChartRef || !this.dashboardStats) return;

    const ctx = this.eventsChartRef.nativeElement.getContext('2d');
    if (!ctx) return;

    const dates = Object.keys(this.dashboardStats.eventsByDate).sort();
    const counts = dates.map(date => this.dashboardStats!.eventsByDate[date]);

    const config: ChartConfiguration = {
      type: this.selectedGraphType === 'pie' || this.selectedGraphType === 'doughnut' ? 'bar' : this.selectedGraphType as any,
      data: {
        labels: dates,
        datasets: [{
          label: 'Events',
          data: counts,
          backgroundColor: 'rgba(75, 192, 192, 0.6)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 2,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: `Events Over Time (${this.periods.find(p => p.value === this.selectedPeriod)?.label})`
          }
        },
        scales: this.selectedGraphType === 'line' || this.selectedGraphType === 'bar' ? {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1
            }
          }
        } : undefined
      }
    };

    this.eventsChart = new Chart(ctx, config);
  }

  private createNotificationsChart(): void {
    if (!this.notificationsChartRef || !this.dashboardStats) return;

    const ctx = this.notificationsChartRef.nativeElement.getContext('2d');
    if (!ctx) return;

    const dates = Object.keys(this.dashboardStats.notificationsByDate).sort();
    const counts = dates.map(date => this.dashboardStats!.notificationsByDate[date]);

    const config: ChartConfiguration = {
      type: this.selectedGraphType === 'pie' || this.selectedGraphType === 'doughnut' ? 'bar' : this.selectedGraphType as any,
      data: {
        labels: dates,
        datasets: [{
          label: 'Notifications',
          data: counts,
          backgroundColor: 'rgba(255, 159, 64, 0.6)',
          borderColor: 'rgba(255, 159, 64, 1)',
          borderWidth: 2,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: `Customer Notifications Over Time (${this.periods.find(p => p.value === this.selectedPeriod)?.label})`
          }
        },
        scales: this.selectedGraphType === 'line' || this.selectedGraphType === 'bar' ? {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1
            }
          }
        } : undefined
      }
    };

    this.notificationsChart = new Chart(ctx, config);
  }

  private createEventTypeChart(): void {
    if (!this.eventTypeChartRef || !this.dashboardStats) return;

    const ctx = this.eventTypeChartRef.nativeElement.getContext('2d');
    if (!ctx) return;

    const types = Object.keys(this.dashboardStats.eventsByType);
    const counts = types.map(type => this.dashboardStats!.eventsByType[type]);

    const colors = this.generateColors(types.length);

    const config: ChartConfiguration = {
      type: this.selectedGraphType as any,
      data: {
        labels: types,
        datasets: [{
          label: 'Events by Type',
          data: counts,
          backgroundColor: colors.background,
          borderColor: colors.border,
          borderWidth: 2,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: `Events by Type (${this.periods.find(p => p.value === this.selectedPeriod)?.label})`
          }
        },
        scales: this.selectedGraphType === 'line' || this.selectedGraphType === 'bar' ? {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1
            }
          }
        } : undefined
      }
    };

    this.eventTypeChart = new Chart(ctx, config);
  }

  private createIndividualEventTypeCharts(): void {
    if (!this.dashboardStats) return;

    // Clear existing charts
    this.eventTypeCharts.forEach(chart => chart.destroy());
    this.eventTypeCharts.clear();

    // Wait for DOM to be ready
    setTimeout(() => {
      this.eventTypes.forEach((type, index) => {
        const canvasId = `eventType_${type.replace(/\s+/g, '_')}`;
        const canvas = document.getElementById(canvasId) as HTMLCanvasElement;
        
        if (canvas) {
          const ctx = canvas.getContext('2d');
          if (!ctx) return;

          const color = this.getColorForIndex(index);

          const config: ChartConfiguration = {
            type: this.selectedGraphType as any,
            data: {
              labels: [type],
              datasets: [{
                label: type,
                data: [this.dashboardStats!.eventsByType[type]],
                backgroundColor: color.background,
                borderColor: color.border,
                borderWidth: 2
              }]
            },
            options: {
              responsive: true,
              maintainAspectRatio: true,
              plugins: {
                legend: {
                  display: false
                },
                title: {
                  display: true,
                  text: `${type} Events`
                }
              },
              scales: this.selectedGraphType === 'line' || this.selectedGraphType === 'bar' ? {
                y: {
                  beginAtZero: true,
                  ticks: {
                    stepSize: 1
                  }
                }
              } : undefined
            }
          };

          const chart = new Chart(ctx, config);
          this.eventTypeCharts.set(type, chart);
        }
      });
    }, 200);
  }

  private destroyCharts(): void {
    if (this.eventsChart) {
      this.eventsChart.destroy();
      this.eventsChart = null;
    }
    if (this.notificationsChart) {
      this.notificationsChart.destroy();
      this.notificationsChart = null;
    }
    if (this.eventTypeChart) {
      this.eventTypeChart.destroy();
      this.eventTypeChart = null;
    }
    this.eventTypeCharts.forEach(chart => chart.destroy());
    this.eventTypeCharts.clear();
  }

  private generateColors(count: number): { background: string[], border: string[] } {
    const backgrounds: string[] = [];
    const borders: string[] = [];

    const baseColors = [
      { r: 75, g: 192, b: 192 },
      { r: 255, g: 99, b: 132 },
      { r: 54, g: 162, b: 235 },
      { r: 255, g: 206, b: 86 },
      { r: 153, g: 102, b: 255 },
      { r: 255, g: 159, b: 64 },
      { r: 199, g: 199, b: 199 },
      { r: 83, g: 102, b: 255 }
    ];

    for (let i = 0; i < count; i++) {
      const color = baseColors[i % baseColors.length];
      backgrounds.push(`rgba(${color.r}, ${color.g}, ${color.b}, 0.6)`);
      borders.push(`rgba(${color.r}, ${color.g}, ${color.b}, 1)`);
    }

    return { background: backgrounds, border: borders };
  }

  private getColorForIndex(index: number): { background: string, border: string } {
    const colors = this.generateColors(index + 1);
    return {
      background: colors.background[index],
      border: colors.border[index]
    };
  }

  ngOnDestroy(): void {
    this.destroyCharts();
  }
}

