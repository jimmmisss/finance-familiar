import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DespesaComponent } from './list/despesa.component';
import { DespesaDetailComponent } from './detail/despesa-detail.component';
import { DespesaUpdateComponent } from './update/despesa-update.component';
import { DespesaDeleteDialogComponent } from './delete/despesa-delete-dialog.component';
import { DespesaRoutingModule } from './route/despesa-routing.module';

@NgModule({
  imports: [SharedModule, DespesaRoutingModule],
  declarations: [DespesaComponent, DespesaDetailComponent, DespesaUpdateComponent, DespesaDeleteDialogComponent],
  entryComponents: [DespesaDeleteDialogComponent],
})
export class DespesaModule {}
