import {Component, Input, OnInit} from '@angular/core';
import {Order} from "../../../../_models/order";
import {User} from "../../../../_models/user";

@Component({
  selector: 'app-client-info',
  templateUrl: './client-info.component.html',
  styleUrls: ['./client-info.component.css']
})
export class ClientInfoComponent implements OnInit {

  @Input() public client: User;

  constructor() { }

  ngOnInit() {
  }

  getClientName() {
    if (this.client) return this.client.fullName;
  }

  getClientPhone() {
    if (this.client) return this.client.phone;
  }
}
