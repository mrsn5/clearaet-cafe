import {Component, OnInit, OnDestroy} from '@angular/core';
import {Order} from "../../_models/order";
import {OrderService} from "../../_services/order.service";
import {PageFilter} from "../../_models/page-filter";
import {OrderFilter} from "../../_models/order-filter";
import {forkJoin, Observable, interval, Subject} from "rxjs";
import {AppComponent} from "../../app.component";
import { OrderState } from 'src/app/_models/order-state';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { withLatestFrom, filter, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit, OnDestroy {
  public orders: Order[];
  public pageSizeOptions = [5, 10, 25, 50, 100];
  public pageFilter = new PageFilter();
  public orderFilter = new OrderFilter();
  public allOrdersCount: number;
  private refreshInterval: Observable<any> = interval(60*1000);
  private destroyed: Subject<any> = new Subject<any>();

  constructor(private orderService: OrderService,
              private app: AppComponent,
              private authService: AuthenticationService) {
    this.pageFilter.page = 0;
    this.pageFilter.size = this.pageSizeOptions[0];
  }
  


  ngOnInit() {
    this.load();
    this.refreshInterval
    .pipe(
      takeUntil(this.destroyed),
      withLatestFrom(this.authService.currentUser),
      filter(([_, u]) => u && u.role === 'admin')
    )
    .subscribe(_ => this.load());
  }

  ngOnDestroy(): void {
    this.destroyed.next();
    this.destroyed.complete();
  }

  changeStateList(source) {
    if (source.checked) {
      this.orderFilter.orderStates.push(source.value);
    } else {
      const index = this.orderFilter.orderStates.indexOf(source.value);
      if (index > -1) {
        this.orderFilter.orderStates.splice(index, 1);
      }
    }
    this.load()
  }

  pageEvent($event) {
    this.pageFilter.size = $event.pageSize;
    this.pageFilter.page = (this.pageFilter.size != $event.pageSize) ? 0 : $event.pageIndex;
    console.log($event);
    this.load();
  }

  processOrderChanged(order: Order) {
    this.orderService.updateState(order).subscribe(_ => this.load());
  }


  load() {
    forkJoin(
      this.orderService.getAll(this.app.currentUser, this.orderFilter, this.pageFilter),
      this.orderService.getCount(this.orderFilter)
    ).subscribe(([orders, count]) => {
      this.orders = orders;
      this.allOrdersCount = count
    });
  }
}
