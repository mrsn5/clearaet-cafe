import { Component, OnInit, ViewChild } from '@angular/core';
import {User} from "../_models/user";
import {UserService} from "../_services/user.service";
import {AppComponent} from "../app.component";
import { Observable, merge, concat, forkJoin, Subject, BehaviorSubject, of } from 'rxjs';
import { Dish } from '../_models/dish';
import { DishesRepository } from '../_services/dishes-repository.service';
import { MatSelectionList, MatListOption, MatSelectionListChange } from '@angular/material/list';
import { SelectionModel } from '@angular/cdk/collections';
import { DishCategory } from '../_models/dish-category';
import { FilterState } from '../_models/filter-state';
import { skip, catchError } from 'rxjs/operators';
import { AuthenticationService } from '../_services/authentication.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Ingredient } from '../_models/ingredient';
import { OrderStateService } from '../_services/order-state.service';
import {OrderService} from "../_services/order.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  @ViewChild(MatSelectionList, {static: true})
  private selectionList: MatSelectionList;
  public loading = false;
  public user: User;
  private dishes: Subject<Dish[]> = new Subject<Dish[]>();
  public categories: DishCategory[] = [];
  public ingredients: Ingredient[] = [];
  private filterState: BehaviorSubject<FilterState> = new BehaviorSubject(new FilterState());
  public dishes$: Observable<Dish[]>;
  public lastData: Dish[];
  public filterShown = false;

  constructor(private userService: UserService,
              private dishesRepository: DishesRepository,
              private authService: AuthenticationService,
              private orderState: OrderStateService) {
    this.dishes$ = this.dishes.asObservable();
    this.dishes.subscribe(data => { this.lastData = data; });
  }

  public get LoggedIn(): boolean {
    return this.user != null;
  }

  ngOnInit() {
    this.loading = true;
    forkJoin(
      this.fetchUser(),
      this.dishesRepository.get(),
      this.dishesRepository.getCategories(),
      this.dishesRepository.getIngredients()
    ).subscribe(([user, dishes, categories, ingredients]) => {
        this.user = user;
        this.dishes.next(dishes);
        this.categories = categories;
        this.ingredients = ingredients;
        this.loading = false;
      });
    this.filterState
      .pipe(skip(1))
      .subscribe(st => this.dishesRepository.get(st).subscribe(dishes => this.dishes.next(dishes)));

  }
  categoriesChanged(change: MatSelectionListChange) {
    const prevState = this.filterState.value;
    const option = change.option;
    const prevCats = prevState.categories;
    const nextprevCats = option.selected ? [...prevCats, option.value] : prevCats.filter(_ => _ !== option.value);
    this.filterState.next({...prevState, categories : nextprevCats})
  }

  ingredientsChanged(ings: number[]) {
    const prevState = this.filterState.value;
    this.filterState.next({...prevState, ingredients : ings})
  }

  public getOrdered(uid: number) : number {
    return this.orderState.getSelected(uid);
  }

  public setOrdered(dish: Dish, amount: number) : void {
    this.orderState.setSelected(dish, amount);
  }

  public delete(dish: Dish) : void {
    this.dishes.next(this.lastData.filter(d => d.uid != dish.uid));
  }

  private fetchUser(): Observable<User> {
    if (!this.authService.currentUserValue) {
      return of(null);
    }
    return this.userService
      .get(this.authService.currentUserValue.uid)
      .pipe(
        catchError((err: HttpErrorResponse) => of(null))
      );
  }

  switchFilters() {
    this.filterShown = !this.filterShown;
    // document.getElementById("filtersBar").style.width = "210px";
    // document.getElementById("menu-container").style.marginLeft = "250px";
  }
}
