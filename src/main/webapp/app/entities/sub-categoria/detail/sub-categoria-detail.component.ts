import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubCategoria } from '../sub-categoria.model';

@Component({
  selector: 'jhi-sub-categoria-detail',
  templateUrl: './sub-categoria-detail.component.html',
})
export class SubCategoriaDetailComponent implements OnInit {
  subCategoria: ISubCategoria | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subCategoria }) => {
      this.subCategoria = subCategoria;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
