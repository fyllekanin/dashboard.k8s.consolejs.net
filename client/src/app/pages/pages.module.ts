import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { pagesRoutes } from './pages.routes';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { PagesComponent } from './pages.component';
import { DashboardOutline, FormOutline, MenuFoldOutline, MenuUnfoldOutline } from '@ant-design/icons-angular/icons';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { CoreModule } from '../core/core.module';
import {NzSelectModule} from "ng-zorro-antd/select";

const icons = [MenuFoldOutline, MenuUnfoldOutline, DashboardOutline, FormOutline];

@NgModule({
    imports: [
        CoreModule,
        RouterModule.forChild(pagesRoutes),
        NzLayoutModule,
        NzMenuModule,
        NzIconModule.forChild(icons),
        NzSelectModule
    ],
    declarations: [
        PagesComponent
    ],
    exports: [
        RouterModule
    ]
})
export class PagesModule {
}
