import { Component } from '@angular/core';
import {User} from "./_models/user";
import {Router} from "@angular/router";
import {AuthenticationService} from "./_services/authentication.service";
import { OrderStateService } from './_services/order-state.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'caprezzy';

  currentUser: User;
  hideItem = true;
  orderedAmount$: Observable<number>;
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private orderStateService: OrderStateService
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    this.orderedAmount$ = this.orderStateService.orderStateChanged$
      .pipe(
        map(() => this.orderStateService
          .getDishes()
          .reduce((i, j) => i + this.orderStateService.getSelected(j), 0))
      )
  }

  isAdmin() {
    return this.currentUser ? this.currentUser.role == 'admin' : false;
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  triggerMenu() {
    var x = document.getElementsByClassName('nav-item');
    for (var i = 0; i < x.length; i++) {
      if (this.hideItem) {
        x[i].classList.remove('small-sc')
      } else {
        x[i].classList.add('small-sc')
      }
    }

    this.hideItem = !this.hideItem;
  }
}
