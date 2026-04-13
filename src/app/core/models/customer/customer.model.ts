import { Address } from '../common/address.model';
import { CustomerIdentity } from './customer-identity.model';
import { CustomerKind } from './customer-kind.model';

export interface CustomerContact {
  email?: string;
  phoneNumber?: string;
}

export interface Customer {
  id: string;
  kind: CustomerKind;
  identity: CustomerIdentity;
  contact?: CustomerContact;
  residenceAddress?: Address;
  correspondenceAddress?: Address;
  isVatPayer?: boolean;
  isForeignClient?: boolean;
}
