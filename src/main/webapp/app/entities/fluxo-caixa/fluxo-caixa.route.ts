import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFluxoCaixa, FluxoCaixa } from 'app/shared/model/fluxo-caixa.model';
import { FluxoCaixaService } from './fluxo-caixa.service';
import { FluxoCaixaComponent } from './fluxo-caixa.component';
import { FluxoCaixaDetailComponent } from './fluxo-caixa-detail.component';
import { FluxoCaixaUpdateComponent } from './fluxo-caixa-update.component';

@Injectable({ providedIn: 'root' })
export class FluxoCaixaResolve implements Resolve<IFluxoCaixa> {
  constructor(private service: FluxoCaixaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFluxoCaixa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fluxoCaixa: HttpResponse<FluxoCaixa>) => {
          if (fluxoCaixa.body) {
            return of(fluxoCaixa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FluxoCaixa());
  }
}

export const fluxoCaixaRoute: Routes = [
  {
    path: '',
    component: FluxoCaixaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.fluxoCaixa.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FluxoCaixaDetailComponent,
    resolve: {
      fluxoCaixa: FluxoCaixaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.fluxoCaixa.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FluxoCaixaUpdateComponent,
    resolve: {
      fluxoCaixa: FluxoCaixaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.fluxoCaixa.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FluxoCaixaUpdateComponent,
    resolve: {
      fluxoCaixa: FluxoCaixaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.fluxoCaixa.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
