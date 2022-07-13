import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDespesa, getDespesaIdentifier } from '../despesa.model';

export type EntityResponseType = HttpResponse<IDespesa>;
export type EntityArrayResponseType = HttpResponse<IDespesa[]>;

@Injectable({ providedIn: 'root' })
export class DespesaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/despesas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(despesa: IDespesa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(despesa);
    return this.http
      .post<IDespesa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(despesa: IDespesa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(despesa);
    return this.http
      .put<IDespesa>(`${this.resourceUrl}/${getDespesaIdentifier(despesa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(despesa: IDespesa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(despesa);
    return this.http
      .patch<IDespesa>(`${this.resourceUrl}/${getDespesaIdentifier(despesa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDespesa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDespesa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDespesaToCollectionIfMissing(despesaCollection: IDespesa[], ...despesasToCheck: (IDespesa | null | undefined)[]): IDespesa[] {
    const despesas: IDespesa[] = despesasToCheck.filter(isPresent);
    if (despesas.length > 0) {
      const despesaCollectionIdentifiers = despesaCollection.map(despesaItem => getDespesaIdentifier(despesaItem)!);
      const despesasToAdd = despesas.filter(despesaItem => {
        const despesaIdentifier = getDespesaIdentifier(despesaItem);
        if (despesaIdentifier == null || despesaCollectionIdentifiers.includes(despesaIdentifier)) {
          return false;
        }
        despesaCollectionIdentifiers.push(despesaIdentifier);
        return true;
      });
      return [...despesasToAdd, ...despesaCollection];
    }
    return despesaCollection;
  }

  protected convertDateFromClient(despesa: IDespesa): IDespesa {
    return Object.assign({}, despesa, {
      dtVcto: despesa.dtVcto?.isValid() ? despesa.dtVcto.format(DATE_FORMAT) : undefined,
      dtPgto: despesa.dtPgto?.isValid() ? despesa.dtPgto.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dtVcto = res.body.dtVcto ? dayjs(res.body.dtVcto) : undefined;
      res.body.dtPgto = res.body.dtPgto ? dayjs(res.body.dtPgto) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((despesa: IDespesa) => {
        despesa.dtVcto = despesa.dtVcto ? dayjs(despesa.dtVcto) : undefined;
        despesa.dtPgto = despesa.dtPgto ? dayjs(despesa.dtPgto) : undefined;
      });
    }
    return res;
  }
}
