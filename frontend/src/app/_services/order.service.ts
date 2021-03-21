import {Injectable} from "@angular/core";
import {Order} from "../_models/order";
import {OrderFilter} from "../_models/order-filter";
import {PageFilter} from "../_models/page-filter";
import {ApiClientService} from "./api-client.service";
import {User} from "../_models/user";
import {LiqButton} from "../_models/liq-button";

@Injectable({ providedIn: 'root' })
export class OrderService {

  constructor(private apiClient: ApiClientService) { }

  getAll(user: User, orderFilter?: OrderFilter, pageFilter?: PageFilter) {
    var path = `api/order`;
    if (user.role != 'admin') {
      path += '/my'
    }

    let filters: string[] = [];
    if (orderFilter && orderFilter.orderStates) {
      filters = filters.concat(orderFilter.orderStates.map(s => `orderStates=${s}`));
    }
    if (pageFilter) {
      if (pageFilter.size) filters.push(`size=${pageFilter.size}`);
      if (pageFilter.page) filters.push(`page=${pageFilter.page}`);
      if (pageFilter.sort) filters.push(`sort=${pageFilter.sort}`);
    }
    const query = filters.join('&');
    const fullUrl = query ? `${path}?${query}` : path;
    return this.apiClient.get<Order[]>(fullUrl);
  }

  get(id) {
    return this.apiClient.get<Order>(`api/order/${id}`);
  }

  getCount(orderFilter?: OrderFilter) {
    const path = `api/order/count`;
    let filters: string[] = [];
    if (orderFilter && orderFilter.orderStates) {
      filters = filters.concat(orderFilter.orderStates.map(s => `orderStates=${s}`));
    }
    const query = filters.join('&');
    const fullUrl = query ? `${path}?${query}` : path;
    return this.apiClient.get<number>(fullUrl);
  }

  updateState(order: Order) {
    return this.apiClient.put<Order>(`api/order/`, order);
  }

  getPaymentButton(id) {
    return this.apiClient.get<LiqButton>(`api/order/${id}/pay`);
  }
}
