import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDespesa } from '../despesa.model';

@Component({
  selector: 'jhi-despesa-detail',
  templateUrl: './despesa-detail.component.html',
})
export class DespesaDetailComponent implements OnInit {
  despesa: IDespesa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ despesa }) => {
      this.despesa = despesa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
