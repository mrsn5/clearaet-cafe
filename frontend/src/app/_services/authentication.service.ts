import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, ReplaySubject } from 'rxjs';
import { map } from 'rxjs/operators';
import {User} from "../_models/user";
import { ApiClientService } from './api-client.service';
import { OrderStateService } from './order-state.service';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  private currentUserReplay: ReplaySubject<User> = new ReplaySubject<User>(1);
  public currentUser: Observable<User>;

  constructor(private apiClient: ApiClientService, private orderStateService: OrderStateService) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserReplay.asObservable();
    this.currentUserSubject.subscribe(this.currentUserReplay);
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  public login(username: string, password: string) {
    return this.apiClient.post<any>(`api/authenticate`, { username, password })
      .pipe(map(user => {
        // store user details and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.orderStateService.clear();
  }
}
