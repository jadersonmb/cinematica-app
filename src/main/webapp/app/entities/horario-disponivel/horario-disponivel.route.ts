import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHorarioDisponivel, HorarioDisponivel } from 'app/shared/model/horario-disponivel.model';
import { HorarioDisponivelService } from './horario-disponivel.service';
import { HorarioDisponivelComponent } from './horario-disponivel.component';
import { HorarioDisponivelDetailComponent } from './horario-disponivel-detail.component';
import { HorarioDisponivelUpdateComponent } from './horario-disponivel-update.component';

@Injectable({ providedIn: 'root' })
export class HorarioDisponivelResolve implements Resolve<IHorarioDisponivel> {
  constructor(private service: HorarioDisponivelService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHorarioDisponivel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((horarioDisponivel: HttpResponse<HorarioDisponivel>) => {
          if (horarioDisponivel.body) {
            return of(horarioDisponivel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HorarioDisponivel());
  }
}

export const horarioDisponivelRoute: Routes = [
  {
    path: '',
    component: HorarioDisponivelComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.horarioDisponivel.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HorarioDisponivelDetailComponent,
    resolve: {
      horarioDisponivel: HorarioDisponivelResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.horarioDisponivel.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HorarioDisponivelUpdateComponent,
    resolve: {
      horarioDisponivel: HorarioDisponivelResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.horarioDisponivel.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HorarioDisponivelUpdateComponent,
    resolve: {
      horarioDisponivel: HorarioDisponivelResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.horarioDisponivel.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
