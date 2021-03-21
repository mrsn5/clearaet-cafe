import { Component, OnInit } from '@angular/core';
import {DishCategory} from "../../../_models/dish-category";
import {DishesRepository} from "../../../_services/dishes-repository.service";
import {FormGroup, NgForm} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css']
})
export class AddCategoryComponent implements OnInit {

  category = new DishCategory();
  loading = false;
  error: any;
  success: any;
  ff: FormGroup;

  constructor(private dishService: DishesRepository,
              private route: ActivatedRoute) { }

  ngOnInit() {
    if (this.route.snapshot.queryParams['categoryId']) {
      this.dishService.getCategoryById(this.route.snapshot.queryParams['categoryId']).subscribe(data => this.category = data)
    }

  }

  addCategory(f: NgForm) {
    console.log(this.category);
    this.dishService.postCategory(this.category).subscribe(
      result => {
        console.log(result);
        this.success = "Категорія " + result.name + " додана успішно!";
        this.category = new DishCategory();
        f.resetForm()
        this.loading = false;
      }, error => {
        this.error = error
        this.loading = false;
      }
    );
    return false;
  }

  submit(ff: NgForm) {
    this.loading = true;
    this.success = null;
    this.error = null;
    if (this.category.uid) {
      this.saveCategory()
    } else {
      this.addCategory(ff);
    }
  }

  private saveCategory() {
    this.dishService.putCategory(this.category).subscribe(data => {
      this.category = data;
      this.success = "Категорiя " + data.name + " збережена успішно!";
      this.loading = false;
    }, err => {
      this.error = err;
      this.loading = false;
    })
  }
}
