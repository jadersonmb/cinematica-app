import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFormaPagamento, FormaPagamento } from 'app/shared/model/forma-pagamento.model';
import { FormaPagamentoService } from './forma-pagamento.service';
import { FormaPagamentoComponent } from './forma-pagamento.component';
import { FormaPagamentoDetailComponent } from './forma-pagamento-detail.component';
import { FormaPagamentoUpdateComponent } from './forma-pagamento-update.component';

@Injectable({ providedIn: 'root' })
export class FormaPagamentoResolve implements Resolve<IFormaPagamento> {
  constructor(private service: FormaPagamentoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormaPagamento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((formaPagamento: HttpResponse<FormaPagamento>) => {
          if (formaPagamento.body) {
            return of(formaPagamento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FormaPagamento());
  }
}

export const formaPagamentoRoute: Routes = [
  {
    path: '',
    component: FormaPagamentoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.formaPagamento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FormaPagamentoDetailComponent,
    resolve: {
      formaPagamento: FormaPagamentoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.formaPagamento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FormaPagamentoUpdateComponent,
    resolve: {
      formaPagamento: FormaPagamentoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.formaPagamento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FormaPagamentoUpdateComponent,
    resolve: {
      formaPagamento: FormaPagamentoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.formaPagamento.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
