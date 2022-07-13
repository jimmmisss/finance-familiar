import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DespesaComponent } from '../list/despesa.component';
import { DespesaDetailComponent } from '../detail/despesa-detail.component';
import { DespesaUpdateComponent } from '../update/despesa-update.component';
import { DespesaRoutingResolveService } from './despesa-routing-resolve.service';

const despesaRoute: Routes = [
  {
    path: '',
    component: DespesaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DespesaDetailComponent,
    resolve: {
      despesa: DespesaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DespesaUpdateComponent,
    resolve: {
      despesa: DespesaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DespesaUpdateComponent,
    resolve: {
      despesa: DespesaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(despesaRoute)],
  exports: [RouterModule],
})
export class DespesaRoutingModule {}
