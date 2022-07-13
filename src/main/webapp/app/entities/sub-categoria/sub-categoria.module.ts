import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SubCategoriaComponent } from './list/sub-categoria.component';
import { SubCategoriaDetailComponent } from './detail/sub-categoria-detail.component';
import { SubCategoriaUpdateComponent } from './update/sub-categoria-update.component';
import { SubCategoriaDeleteDialogComponent } from './delete/sub-categoria-delete-dialog.component';
import { SubCategoriaRoutingModule } from './route/sub-categoria-routing.module';

@NgModule({
  imports: [SharedModule, SubCategoriaRoutingModule],
  declarations: [SubCategoriaComponent, SubCategoriaDetailComponent, SubCategoriaUpdateComponent, SubCategoriaDeleteDialogComponent],
  entryComponents: [SubCategoriaDeleteDialogComponent],
})
export class SubCategoriaModule {}
