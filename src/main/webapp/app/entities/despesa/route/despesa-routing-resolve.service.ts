import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDespesa, Despesa } from '../despesa.model';
import { DespesaService } from '../service/despesa.service';

@Injectable({ providedIn: 'root' })
export class DespesaRoutingResolveService implements Resolve<IDespesa> {
  constructor(protected service: DespesaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDespesa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((despesa: HttpResponse<Despesa>) => {
          if (despesa.body) {
            return of(despesa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Despesa());
  }
}
