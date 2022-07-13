import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubCategoria } from '../sub-categoria.model';
import { SubCategoriaService } from '../service/sub-categoria.service';

@Component({
  templateUrl: './sub-categoria-delete-dialog.component.html',
})
export class SubCategoriaDeleteDialogComponent {
  subCategoria?: ISubCategoria;

  constructor(protected subCategoriaService: SubCategoriaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subCategoriaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
