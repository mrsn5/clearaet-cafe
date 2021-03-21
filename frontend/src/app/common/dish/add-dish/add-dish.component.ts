import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Dish} from "../../../_models/dish";
import {DishesRepository} from "../../../_services/dishes-repository.service";
import {DishIngredient} from "../../../_models/dish-ingredient";
import {Ingredient} from "../../../_models/ingredient";
import {DishCategory} from "../../../_models/dish-category";
import {NgForm} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-add-dish',
  templateUrl: './add-dish.component.html',
  styleUrls: ['./add-dish.component.css']
})
export class AddDishComponent implements OnInit {

  public dish = new Dish();
  error: any;
  loading = false;
  newIngredient = new Ingredient();
  newQuantity = 0;
  file;
  allCategories = [];
  newCategory = new DishCategory();
  filename: "Choose file";
  success: any;
  newQuantitySubmitted = false;
  quantityInvalid = false;


  constructor(private dishService: DishesRepository,
              private route: ActivatedRoute) {}

  ngOnInit() {
    this.reloadCategories();
    if (this.route.snapshot.queryParams['dishId']) {
      this.dishService.getById(this.route.snapshot.queryParams['dishId']).subscribe(data => this.dish = data)
    }
  }

  submit(f: NgForm) {
    this.success = null;
    this.error = null;
    this.loading = true;
    if (this.dish.uid) {
      this.saveDish()
    } else {
      this.addDish(f);
    }
  }

  saveDish() {
    const formData = new FormData();
    formData.append('file', this.file ? this.file : null);
    formData.append('dish', JSON.stringify(this.dish));
    this.dishService.putDish(formData).subscribe(data => {
      this.dish = data;
      this.success = "Страва " + data.name + " збережена успішно!";
      this.loading = false;
    }, err => {
      this.error = err
      this.loading = false;
    })
  }

  addDish(f: NgForm) {
    if (!this.file){
      this.error = "Фото обов'язкове поле"
      this.loading = false
      return
    }
    const formData = new FormData();
    formData.append('file', this.file);
    formData.append('dish', JSON.stringify(this.dish));
    this.dishService.postDish(formData).subscribe(
      result => {
        console.log(result);
        this.success = "Страва " + result.name + " додана успішно!";
        this.dish = new Dish();
        this.file = null;
        f.resetForm()
        this.loading = false;
      }, error => {
        this.error = error
        this.loading = false;
      }
    );
  }

  delete(row: DishIngredient) {
    var removeIndex = this.dish.dishIngredients.indexOf(row);
    this.dish.dishIngredients.splice(removeIndex, 1);
    console.log(this.dish.dishIngredients);
  }


  fileChange(event) {
    if (event.target.files.length > 0) {
        this.file = event.target.files[0];
        this.filename = this.file.name
    }
  }

  addIngredient() {
    console.log(parseFloat(String(this.newQuantity)))

    this.newQuantitySubmitted = true;
    if (this.newIngredient.name
      && this.newIngredient.name.trim() != ''
      && this.newQuantity == parseFloat(String(this.newQuantity))
    )
    {
      this.newQuantity = parseFloat(String(this.newQuantity));
      this.dish.dishIngredients.push({
        quantity: this.newQuantity,
        ingredient: this.newIngredient
      });
      this.newIngredient = new Ingredient();
      this.newQuantity = 0;
      this.newQuantitySubmitted = false
    }

  }

  deleteCategory(dc: DishCategory) {
    var removeIndex = this.dish.categories.indexOf(dc);
    this.dish.categories.splice(removeIndex, 1);
    console.log(this.dish.categories);
  }

  addCategory() {
    if (this.dish.categories.indexOf(this.newCategory) == -1 && this.newCategory.name)
      this.dish.categories.push(this.newCategory);
      console.log(this.dish.categories);
    this.newCategory = new DishCategory()
  }

  reloadCategories() {
    console.log('reload');
    this.dishService.getCategories().subscribe(data => {
      this.allCategories = data;
      this.newCategory= data[0];
    })
  }
}
