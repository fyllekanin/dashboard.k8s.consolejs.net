import { Routes } from '@angular/router';
import { AuthGuard } from './core/services/auth.guard';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'page'
    },
    {
        path: 'page',
        canActivate: [
            AuthGuard
        ],
        loadChildren: () => import('./pages/pages.module').then(m => m.PagesModule)
    },
    {
        path: 'auth',
        loadChildren: () => import('./auth/login.module').then(m => m.LoginModule)
    }
];

