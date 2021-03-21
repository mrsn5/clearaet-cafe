import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Order, OrderStatus, PaymentState} from "../../../_models/order";
import {OrderService} from "../../../_services/order.service";
import { OrderStateService } from 'src/app/_services/order-state.service';
import { Router } from '@angular/router';
import { BsModalService } from 'ngx-bootstrap/modal';
import { OrderContentsComponent } from '../../order-contents/order-contents.component';
import { OrderState } from 'src/app/_models/order-state';
@Component({
  selector: 'app-order-info-card',
  templateUrl: './order-info-card.component.html',
  styleUrls: ['./order-info-card.component.css']
})
export class OrderInfoCardComponent implements OnInit {

  @Input() public order: Order;
  @Output() public readonly orderChanged: EventEmitter<Order> = new EventEmitter<Order>();
  constructor(
    private orderService: OrderService,
    private orderStateService: OrderStateService,
    private router: Router,
    private modal: BsModalService
    ) { }

  ngOnInit() {
  }

  getPaymentState() {
    if (this.isPaid()) return "оплачено";
    if (this.order.paymentState == PaymentState.partPaid) return "оплачено частково: " + this.order.paid + " грн";
    if (this.order.paymentState == PaymentState.notPaid) return "неоплачено";
  }

  redo() {
    this.orderStateService.clear();
    this.order.portions.forEach(portion => {
      this.orderStateService.setSelected(portion.dish, portion.quantity);
    });
    this.router.navigate(['/order']);
  }


  update(newOrder) {
    console.log('updating ' + newOrder);
    this.orderService.updateState(newOrder).pipe().subscribe(o => this.order = o);
  }

  changeContents() {
    let state: OrderState = {};
    this.order.portions.forEach(p => state[p.dish.uid] = {price: p.dish.price, quantity: p.quantity})
    const ref = this.modal.show(
      OrderContentsComponent, 
      {initialState: { orderStateOriginal: state, orderId: this.order.uid }, 
      ignoreBackdropClick: true
    });
    (<OrderContentsComponent>ref.content).orderSaved.subscribe(os => {
      const order = {...this.order};
      order.portions = Object.keys(os).map(key => <any>{
          dish: {
              uid: key
          },
          quantity: os[key].quantity
      });
      this.orderChanged.emit(order)
    });
  }


  //update order state
  updateState(status: OrderStatus) {
    this.update({uid: this.order.uid, orderState: status});
  }

  confirm() {
    this.updateState(OrderStatus.confirmed);
  }

  cooking() {
    this.updateState(OrderStatus.inProgress);
  }

  ready() {
    this.updateState(OrderStatus.ready);
  }

  done() {
    this.updateState(OrderStatus.tookAway);
  }

  cancel() {
    this.updateState(OrderStatus.cancelled);
  }


  

  // buttons visibility
  canBeCancelled() {
    return this.order.orderState != OrderStatus.tookAway && this.order.orderState != OrderStatus.cancelled
  }

  canBeConfirmed() {
    return this.order.orderState == OrderStatus.inCheck
  }

  canBeSetInProgress() {
    return this.order.orderState == OrderStatus.confirmed
  }

  canBeSetReady() {
    return this.order.orderState == OrderStatus.inProgress
  }

  canBeDone() {
    return this.order.orderState == OrderStatus.ready
  }

  //helper methods
  isPaid() {
    return this.order.paymentState == PaymentState.fullyPaid;
  }

  getStatus() {
    switch (this.order.orderState) {
      case OrderStatus.cancelled:
        return "Відмінено";
      case OrderStatus.confirmed:
        return "Підтвердженно";
      case OrderStatus.inCheck:
        return "Очікує підтвердження";
      case OrderStatus.inProgress:
        return "Готується";
      case OrderStatus.ready:
        return "Готово";
      case OrderStatus.tookAway:
        return "Закрито";
    }
  }
}
