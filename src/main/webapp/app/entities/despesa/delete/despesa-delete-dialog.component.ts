import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDespesa } from '../despesa.model';
import { DespesaService } from '../service/despesa.service';

@Component({
  templateUrl: './despesa-delete-dialog.component.html',
})
export class DespesaDeleteDialogComponent {
  despesa?: IDespesa;

  constructor(protected despesaService: DespesaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.despesaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
