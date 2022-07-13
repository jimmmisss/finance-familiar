import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubCategoriaComponent } from '../list/sub-categoria.component';
import { SubCategoriaDetailComponent } from '../detail/sub-categoria-detail.component';
import { SubCategoriaUpdateComponent } from '../update/sub-categoria-update.component';
import { SubCategoriaRoutingResolveService } from './sub-categoria-routing-resolve.service';

const subCategoriaRoute: Routes = [
  {
    path: '',
    component: SubCategoriaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubCategoriaDetailComponent,
    resolve: {
      subCategoria: SubCategoriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubCategoriaUpdateComponent,
    resolve: {
      subCategoria: SubCategoriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubCategoriaUpdateComponent,
    resolve: {
      subCategoria: SubCategoriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subCategoriaRoute)],
  exports: [RouterModule],
})
export class SubCategoriaRoutingModule {}
