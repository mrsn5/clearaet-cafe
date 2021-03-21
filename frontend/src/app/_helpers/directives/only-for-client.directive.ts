import { Directive, ViewContainerRef, TemplateRef } from '@angular/core';
import { AuthenticationService } from '../../_services/authentication.service';

@Directive({
  selector: '[appOnlyForClient]'
})
export class OnlyForClientDirective {

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUser.subscribe(u => {
      if (!u || u.role !== 'user') {
        this.viewContainer.clear();
      } else  {
        this.viewContainer.createEmbeddedView(this.templateRef);
      }
    });
  }

}
