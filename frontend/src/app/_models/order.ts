import {User} from "./user";
import {Portion} from "./portion";

export enum OrderStatus {
  inCheck = "in_check",
  confirmed = "confirmed",
  inProgress = "in_progress",
  ready = "ready",
  tookAway = "took_away",
  cancelled = "cancelled"
}

export enum PaymentState {
  notPaid = "not_paid",
  fullyPaid = "fully_paid",
  partPaid = "part_paid"
}

export class Order {
  public uid: number;
  public orderTime: Date;
  public readyTime?: Date;
  public prefTime: Date;
  public preferences?: string;
  public total: number;
  public paid: number;
  public client: User;
  public portions: Portion[];

  public paymentState: PaymentState;
  public orderState: OrderStatus;

}
