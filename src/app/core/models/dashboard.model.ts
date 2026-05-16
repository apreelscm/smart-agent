export interface QuickAction {
  id: string;
  label: string;
  icon: string;
  locked: boolean;
}

export interface KpiCard {
  id: string;
  title: string;
  count?: number;
  description?: string;
  subTextPrefix?: string;
  subTextSuffix?: string;
  emptyText?: string;
  actionLabel: string;
  actionRoute: string;
}

export interface DashboardMessage {
  id: string;
  title: string;
  body: string;
  date: string;
  read: boolean;
}
