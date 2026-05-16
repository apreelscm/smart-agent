export interface Agent {
  id: string;
  name: string;
  code: string;
  packageName: string;
  avatarInitials: string;
  email: string;
  username: string;
  company: string;
}

export interface NavItem {
  label: string;
  icon: string;
  route: string;
  badge?: number;
  section: 'sprzedaz' | 'wkrotce';
  disabled?: boolean;
  children?: NavChild[];
}

export interface NavChild {
  label: string;
  route: string;
  icon?: string;
}
