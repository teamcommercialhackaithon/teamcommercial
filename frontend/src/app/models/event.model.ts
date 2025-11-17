export interface Event {
  eventId?: number;
  type?: string;
  macAddress?: string;
  serial?: string;
  message?: string;
  date: string;
  payloadBase64?: string;
  payloadSize?: number;
  createdAt?: string;
  updatedAt?: string;
}

