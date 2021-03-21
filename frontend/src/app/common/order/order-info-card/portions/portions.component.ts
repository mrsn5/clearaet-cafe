import {Component, Input, OnInit} from '@angular/core';
import {Portion} from "../../../../_models/portion";

@Component({
  selector: 'app-portions',
  templateUrl: './portions.component.html',
  styleUrls: ['./portions.component.css']
})
export class PortionsComponent implements OnInit {
  @Input() public portions: Portion[];
  @Input() public prefs: string;
  constructor() { }

  ngOnInit() {
  }

}
