import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { OrderState } from 'src/app/_models/order-state';
import { BsModalRef } from "ngx-bootstrap/modal"
import { Dish } from 'src/app/_models/dish';
import { DishesRepository } from 'src/app/_services/dishes-repository.service';
import { DishDisplayMode } from '../dish/dish.component';

@Component({
  selector: 'app-order-contents',
  templateUrl: './order-contents.component.html',
  styleUrls: ['./order-contents.component.css']
})
export class OrderContentsComponent implements OnInit {

  public orderStateOriginal: OrderState;
  public orderStateCurrent: OrderState;
  public orderId: number;
  public displayMode: DishDisplayMode = DishDisplayMode.Compact;
  @Output() public readonly orderSaved: EventEmitter<OrderState> = new EventEmitter<OrderState>();
  private dishes: Dish[];

  constructor(public modalRef: BsModalRef, private dishesRepository: DishesRepository) { }

  public ngOnInit() {
    this.orderStateCurrent = {...this.orderStateOriginal};
    this.dishesRepository.get().subscribe(dishes => this.dishes = dishes);
  }

  public get OrderedDishes(){
    return (this.dishes || []).filter(d => d.isAvailable).sort(d => this.getOrdered(d.uid));
  }

  public get CanSave() {
    return (!Object.keys(this.orderStateOriginal)
              .reduce((prev, key) => 
                prev && 
                this.orderStateCurrent[key] && 
                this.orderStateOriginal[key].quantity === this.orderStateCurrent[key].quantity, true) ||
            Object.keys(this.orderStateOriginal).length !== Object.keys(this.orderStateCurrent).length) &&
            Object.keys(this.orderStateCurrent).length !== 0; 
  }

  public getOrdered(uid: number) {
    return this.orderStateCurrent[uid] ? this.orderStateCurrent[uid].quantity : 0;
  }

  public setOrdered(dish, quantity) {
    if(quantity === 0) {
      delete this.orderStateCurrent[dish.uid];
    } else {
      this.orderStateCurrent[dish.uid] = {quantity: quantity, price: dish.price};
    }
  }

  public save() {
    this.orderSaved.emit(this.orderStateCurrent);
    this.modalRef.hide();
  }


}
