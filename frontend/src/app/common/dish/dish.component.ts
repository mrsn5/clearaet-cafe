import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Dish } from '../../_models/dish';
import {DishesRepository} from "../../_services/dishes-repository.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatInputModule} from '@angular/material/input';
import {FormControl} from '@angular/forms';
import {MatAutocompleteSelectedEvent, MatAutocomplete} from '@angular/material/autocomplete';
import {MatChipInputEvent} from '@angular/material/chips';


export enum DishDisplayMode {
  Full,
  Compact
}

@Component({
    selector: 'dish',
    templateUrl: './dish.component.html',
    styleUrls: ['./dish.component.css']
  })
export class DishComponent {
    @Input() public dish: Dish;
    @Input() public ordered: number = 0;
    @Input() public displayMode: DishDisplayMode = DishDisplayMode.Full;
    @Output() public readonly orderedChange: EventEmitter<number> = new EventEmitter<number>();
    @Output() public readonly deleteHandler: EventEmitter<Dish> = new EventEmitter<Dish>();
    public DishDisplayMode = DishDisplayMode;

    constructor(private dishService: DishesRepository,
                private router: Router) { }

    public get unit() {
      return this.dish.categories.some(c => c.uid === 4) ?
      'мл' :
      'гр';
    }

    public get contents() {
      return this.dish.dishIngredients.map(i => i.ingredient.name.toLowerCase()).join(', ');
    }

    public get backgroundImage() {
      return `url(${this.dish.photo})`;
    }

    public get categories() {
      return '#' + this.dish
            .categories.map(i => i.name.toLowerCase()).join(' #');
    }

  delete() {
      this.dishService.delete(this.dish.uid).subscribe( _ => this.deleteHandler.emit(this.dish))
  }

  show() {
    this.dish.isAvailable = true;
    const formData = new FormData();
    formData.append('dish', JSON.stringify(this.dish));
    this.dishService.putDish(formData).subscribe(data => this.dish = data);
  }

  hide() {
    this.dish.isAvailable = false;
    const formData = new FormData();
    formData.append('dish', JSON.stringify(this.dish));
    this.dishService.putDish(formData).subscribe(data => this.dish = data);
  }

  edit() {
    this.router.navigate(["/add"], { queryParams: { dishId: this.dish.uid } });
  }

  minusOrdered() {
      if (this.ordered > 0) {
        this.ordered--
      }
    this.orderedChange.emit(this.ordered)
  }

  plusOrdered() {
    this.ordered++
    this.orderedChange.emit(this.ordered)
  }

  processNumberInput(event) {
    event.target.value = event.target.value.replace(/^0+/, '');
    const ordered = Number(event.target.value);
    if(Number.isNaN(ordered)) {
      event.preventDefault();
      return;
    }
    this.orderedChange.emit(Number(ordered))
  }
}
