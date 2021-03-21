import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "./home/home.component";
import { AuthGuard } from "./_helpers/auth.guard";
import {LoginComponent} from "./login/login.component";
import { OrderComponent } from './order/order.component';
import { OrderGuard } from './_helpers/order.guard';
import {OrderListComponent} from "./common/order-list/order-list.component";
import {RegistrationComponent} from "./registration/registration.component";
import {OrderViewComponent} from "./common/order-view/order-view.component";
import {AdminPageComponent} from "./admin-page/admin-page.component";
import {HistoryComponent} from "./history/history.component";
import {AddDishComponent} from "./common/dish/add-dish/add-dish.component";
import {AddPageComponent} from "./add-page/add-page.component";
import {ConfirmedOrderComponent} from "./confirmed-order/confirmed-order.component";



const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrationComponent },
  { path: 'order', component: OrderComponent, canActivate: [AuthGuard] },
  { path: 'order/:id', component: OrderViewComponent},
  { path: 'admin', component: AdminPageComponent },
  { path: 'history', component: HistoryComponent },
  { path: 'add', component: AddPageComponent },
  { path: 'order/confirm/:id', component: ConfirmedOrderComponent },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

export const appRoutingModule = RouterModule.forRoot(routes, { useHash: true });
