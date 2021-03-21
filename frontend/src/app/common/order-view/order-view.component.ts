import { Component, OnInit } from '@angular/core';
import {Order} from "../../_models/order";
import {ActivatedRoute, Router} from "@angular/router";
import {OrderService} from "../../_services/order.service";

@Component({
  selector: 'app-order-view',
  templateUrl: './order-view.component.html',
  styleUrls: ['./order-view.component.css']
})
export class OrderViewComponent implements OnInit {
  loading = true;
  order: Order;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private orderService: OrderService) { }

  ngOnInit() {
    let id = this.route.snapshot.params['id'];
    this.orderService.get(id).pipe().subscribe(
      order => {
        this.order = order;
        this.loading = false;
      }, error => {
        console.log('ERROR: ' + error)
      }
    )
  }

}
