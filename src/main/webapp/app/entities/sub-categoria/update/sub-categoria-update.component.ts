import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISubCategoria, SubCategoria } from '../sub-categoria.model';
import { SubCategoriaService } from '../service/sub-categoria.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';

@Component({
  selector: 'jhi-sub-categoria-update',
  templateUrl: './sub-categoria-update.component.html',
})
export class SubCategoriaUpdateComponent implements OnInit {
  isSaving = false;

  categoriasSharedCollection: ICategoria[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    descricao: [],
    categoria: [],
  });

  constructor(
    protected subCategoriaService: SubCategoriaService,
    protected categoriaService: CategoriaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subCategoria }) => {
      this.updateForm(subCategoria);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subCategoria = this.createFromForm();
    if (subCategoria.id !== undefined) {
      this.subscribeToSaveResponse(this.subCategoriaService.update(subCategoria));
    } else {
      this.subscribeToSaveResponse(this.subCategoriaService.create(subCategoria));
    }
  }

  trackCategoriaById(_index: number, item: ICategoria): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubCategoria>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(subCategoria: ISubCategoria): void {
    this.editForm.patchValue({
      id: subCategoria.id,
      nome: subCategoria.nome,
      descricao: subCategoria.descricao,
      categoria: subCategoria.categoria,
    });

    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing(
      this.categoriasSharedCollection,
      subCategoria.categoria
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing(categorias, this.editForm.get('categoria')!.value)
        )
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));
  }

  protected createFromForm(): ISubCategoria {
    return {
      ...new SubCategoria(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      categoria: this.editForm.get(['categoria'])!.value,
    };
  }
}
