import { Directive, ElementRef, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthenticationService } from '../../_services/authentication.service';

@Directive({
  selector: '[appOnlyForAdmin]'
})
export class OnlyForAdminDirective {

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private authenticationService: AuthenticationService
    ) {
      this.authenticationService.currentUser.subscribe(u => {
        if (!u || u.role !== 'admin') {
          this.viewContainer.clear();
        } else  {
          this.viewContainer.createEmbeddedView(this.templateRef);
        }
      });
    }
}
