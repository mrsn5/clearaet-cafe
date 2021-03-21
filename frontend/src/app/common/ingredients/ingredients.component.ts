import { Component, ElementRef, ViewChild, Input, EventEmitter, Output } from '@angular/core';
import { ENTER } from '@angular/cdk/keycodes';
import {FormControl} from '@angular/forms';
import {MatAutocompleteSelectedEvent, MatAutocomplete} from '@angular/material/autocomplete';
import {MatChipInputEvent} from '@angular/material/chips';
import {Observable, Subject, BehaviorSubject, combineLatest} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { Ingredient } from 'src/app/_models/ingredient';
@Component({
  selector: 'ingredients',
  templateUrl: './ingredients.component.html',
  styleUrls: ['./ingredients.component.css']
})
export class IngredientsComponent {
  visible = true;
  selectable = true;
  removable = true;
  separatorKeysCodes: number[] = [ENTER];
  fruitCtrl = new FormControl();
  filteredIngredients: Observable<Ingredient[]>;
  selectedIngredients$: Observable<Ingredient[]>;
  selectedIngredients: BehaviorSubject<Ingredient[]> = new BehaviorSubject<Ingredient[]>([]);
  @Input() allIngredients: Ingredient[] = [];
  @Output() readonly selectionChanged: EventEmitter<number[]> = new EventEmitter<number[]>();

  @ViewChild('fruitInput', {static: true}) fruitInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', {static: true}) matAutocomplete: MatAutocomplete;
  constructor() {
    this.selectedIngredients$ = this.selectedIngredients.asObservable();
    this.selectedIngredients$.subscribe(ings => 
      this.selectionChanged.emit(ings.map(_ => _.uid)));
    this.filteredIngredients = combineLatest(
      this.fruitCtrl.valueChanges.pipe(startWith(null)),
      this.selectedIngredients$.pipe(startWith([]))
    )
    .pipe(
      map(([ing, _]) => {
        if(!ing) {
          return this.notSelectedIngredients();
        }
        if(typeof(ing) === 'string') {
          return this.filter(ing);
        } else {
          return this.filter((<Ingredient>ing).name);
        }
      })
    )
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const found = this.allIngredients.find(ing => ing.name === event.input.value);
    if(!found) {
      return;
    }
    this.selectedIngredients.next([...this.selectedIngredients.value, found]);
    input.value = '';
    this.fruitCtrl.setValue(null);
  }

  remove(ing: Ingredient): void {
    this.selectedIngredients.next(this.selectedIngredients.value.filter(ing1 => ing1.uid !== ing.uid));
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    const found = this.allIngredients.find(ing => ing.uid.toString() === event.option.value.uid.toString());
    this.selectedIngredients.next([...this.selectedIngredients.value, found]);
    this.fruitInput.nativeElement.value = '';
    this.fruitCtrl.setValue(null);
  }

  private filter(value: string): Ingredient[] {
    const filterValue = value.toLowerCase();

    return this.notSelectedIngredients()
      .filter(ing => ing.name.toLowerCase().indexOf(filterValue) === 0);
  }

  private notSelectedIngredients(): Ingredient[] {
    return this.allIngredients.filter(ing1 => 
      !this.selectedIngredients.value.some(ing2 => ing1.name === ing2.name));
  }
}
