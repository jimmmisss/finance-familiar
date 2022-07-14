import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDespesa, Despesa } from '../despesa.model';
import { DespesaService } from '../service/despesa.service';
import { ISubCategoria } from 'app/entities/sub-categoria/sub-categoria.model';
import { SubCategoriaService } from 'app/entities/sub-categoria/service/sub-categoria.service';
import { Mes } from 'app/entities/enumerations/mes.model';
import { FormaPgto } from 'app/entities/enumerations/forma-pgto.model';
import { Responsavel } from 'app/entities/enumerations/responsavel.model';

@Component({
  selector: 'jhi-despesa-update',
  templateUrl: './despesa-update.component.html',
})
export class DespesaUpdateComponent implements OnInit {
  isSaving = false;
  mesValues = Object.keys(Mes);
  formaPgtoValues = Object.keys(FormaPgto);
  responsavelValues = Object.keys(Responsavel);

  subCategoriasSharedCollection: ISubCategoria[] = [];

  editForm = this.fb.group({
    id: [],
    mes: [],
    nome: [],
    descricao: [],
    valor: [],
    dtVcto: [],
    dtPgto: [],
    formaPgto: [],
    responsavel: [],
    dtCadastro: [],
    subCategoria: [],
  });

  constructor(
    protected despesaService: DespesaService,
    protected subCategoriaService: SubCategoriaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ despesa }) => {
      if (despesa.id === undefined) {
        const today = dayjs().startOf('day');
        despesa.dtCadastro = today;
      }

      this.updateForm(despesa);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const despesa = this.createFromForm();
    if (despesa.id !== undefined) {
      this.subscribeToSaveResponse(this.despesaService.update(despesa));
    } else {
      this.subscribeToSaveResponse(this.despesaService.create(despesa));
    }
  }

  trackSubCategoriaById(_index: number, item: ISubCategoria): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDespesa>>): void {
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

  protected updateForm(despesa: IDespesa): void {
    this.editForm.patchValue({
      id: despesa.id,
      mes: despesa.mes,
      nome: despesa.nome,
      descricao: despesa.descricao,
      valor: despesa.valor,
      dtVcto: despesa.dtVcto,
      dtPgto: despesa.dtPgto,
      formaPgto: despesa.formaPgto,
      responsavel: despesa.responsavel,
      dtCadastro: despesa.dtCadastro ? despesa.dtCadastro.format(DATE_TIME_FORMAT) : null,
      subCategoria: despesa.subCategoria,
    });

    this.subCategoriasSharedCollection = this.subCategoriaService.addSubCategoriaToCollectionIfMissing(
      this.subCategoriasSharedCollection,
      despesa.subCategoria
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subCategoriaService
      .query()
      .pipe(map((res: HttpResponse<ISubCategoria[]>) => res.body ?? []))
      .pipe(
        map((subCategorias: ISubCategoria[]) =>
          this.subCategoriaService.addSubCategoriaToCollectionIfMissing(subCategorias, this.editForm.get('subCategoria')!.value)
        )
      )
      .subscribe((subCategorias: ISubCategoria[]) => (this.subCategoriasSharedCollection = subCategorias));
  }

  protected createFromForm(): IDespesa {
    return {
      ...new Despesa(),
      id: this.editForm.get(['id'])!.value,
      mes: this.editForm.get(['mes'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      dtVcto: this.editForm.get(['dtVcto'])!.value,
      dtPgto: this.editForm.get(['dtPgto'])!.value,
      formaPgto: this.editForm.get(['formaPgto'])!.value,
      responsavel: this.editForm.get(['responsavel'])!.value,
      dtCadastro: this.editForm.get(['dtCadastro'])!.value ? dayjs(this.editForm.get(['dtCadastro'])!.value, DATE_TIME_FORMAT) : undefined,
      subCategoria: this.editForm.get(['subCategoria'])!.value,
    };
  }
}
