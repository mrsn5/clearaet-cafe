import { Component, OnInit } from '@angular/core';
import {User} from "../_models/user";
import {UserService} from "../_services/user.service";
import {Validators} from "@angular/forms";
import {AuthenticationService} from "../_services/authentication.service";
import {first} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  user: User = new User();
  error: any;
  loading = false;

  constructor(private userService: UserService,
              private authenticationService: AuthenticationService,
              private route: ActivatedRoute,
              private router: Router,) { }

  register() {
    this.loading = true;
    this.userService.register(this.user).subscribe(
      _ => {
        this.loading = false;
        this.authenticationService.login(this.user.mail, this.user.password)
          .pipe(first())
          .subscribe(
            data => {
              this.router.navigate(['/']);
            },
            error => {
              this.error = error;
              this.loading = false;
            });
      }, error => {
        this.error = error;
        this.loading = false;
      });
  }

  ngOnInit() {}
}
