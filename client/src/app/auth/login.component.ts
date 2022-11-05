import { Component } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { AuthService } from '../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-auth-login',
    templateUrl: 'login.component.html',
    styleUrls: ['login.component.scss']
})
export class LoginComponent {
    validateForm!: UntypedFormGroup;

    constructor(
        private untypedFormBuilder: UntypedFormBuilder,
        private authService: AuthService,
        private router: Router
    ) {
    }

    submitForm(): void {
        if (this.validateForm.valid) {
            this.authService.isLoggedIn = true;
            this.router.navigateByUrl('/page/welcome');
        } else {
            Object.values(this.validateForm.controls).forEach(control => {
                if (control.invalid) {
                    control.markAsDirty();
                    control.updateValueAndValidity({ onlySelf: true });
                }
            });
        }
    }

    ngOnInit(): void {
        this.validateForm = this.untypedFormBuilder.group({
            password: [null, [Validators.required]]
        });
    }
}
