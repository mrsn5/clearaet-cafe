import {Component, OnDestroy, OnInit} from '@angular/core';
import {OrderStateService} from "../_services/order-state.service";
import {DishesRepository} from "../_services/dishes-repository.service";
import {FormBuilder} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiClientService} from "../_services/api-client.service";
import {AuthenticationService} from "../_services/authentication.service";
import {DomSanitizer} from "@angular/platform-browser";
import {OrderService} from "../_services/order.service";
import {interval} from "rxjs";
import { PausableObservable, pausable } from 'rxjs-pausable';

@Component({
  selector: 'app-confirmed-order',
  templateUrl: './confirmed-order.component.html',
  styleUrls: ['./confirmed-order.component.css']
})
export class ConfirmedOrderComponent implements OnInit, OnDestroy {

  public liqpayHtml = "";
  public card = false;
  public id = 0;

  constructor(
    public orderState: OrderStateService,
    private api: ApiClientService,
    private authenticationService: AuthenticationService,
    private sanitizer: DomSanitizer,
    private orderService: OrderService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.card = this.route.snapshot.params['card'] == 'true';

    if (this.card) {
      this.orderService.getPaymentButton(this.id).pipe().subscribe(button => {
        this.liqpayHtml = button.html;
      });
    }


    this.shoot();
    this.pausable = interval(800)
      .pipe(pausable()) as PausableObservable<number>;
    this.pausable.subscribe(this.shoot.bind(this));
    this.pausable.resume();
  }

  ngOnDestroy() {
    this.pausable.pause();
  }

  pausable: PausableObservable<number>;

  shoot() {
    try {
      this.confetti({
        angle: this.random(30, 150),
        spread: this.random(30, 80),
        particleCount: this.random(40, 50),
        origin: {
          y: 0.6
        },
        colors: ['#555877', '#9FA2C4']
      });
    } catch(e) {
      console.log('no confetti')
    }
  }



  random(min: number, max: number) {
    return Math.random() * (max - min) + min;
  }

  confetti(args: any) {
    return window['confetti'].apply(this, arguments);
  }

}
