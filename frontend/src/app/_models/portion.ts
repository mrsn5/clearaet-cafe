import {Dish} from "./dish";

export class Portion {
  public uid: number;
  public isCancelled: boolean;
  public dish: Dish;
  public quantity: number;
  public price: number;
}
