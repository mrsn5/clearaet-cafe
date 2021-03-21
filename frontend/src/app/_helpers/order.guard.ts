import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { OrderStateService } from '../_services/order-state.service';
import { Injectable } from '@angular/core';

@Injectable()
export class OrderGuard implements CanActivate {
   
    constructor(
        private orderState: OrderStateService,
        private router: Router
        ) { }
    canActivate(_: ActivatedRouteSnapshot, __: RouterStateSnapshot) : boolean  {
        if(this.orderState.defined()) {
            return true;
        } else {
            this.router.navigate(['/']);
            return false;
        }
    }

}