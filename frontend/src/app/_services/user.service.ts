import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {User} from "../_models/user";
import {environment} from "../../environments/environment";


@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) { }

  get(id) {
    console.log(`${environment.apiUrl}/user/${id}`)
    return this.http.get<User>(`${environment.apiUrl}/api/user/${id}`);
  }


  register(user: User) {
    return this.http.post<User>(`${environment.apiUrl}/api/register`, user);
  }
}
