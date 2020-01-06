import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHorario, Horario } from 'app/shared/model/horario.model';
import { HorarioService } from './horario.service';
import { HorarioComponent } from './horario.component';
import { HorarioDetailComponent } from './horario-detail.component';
import { HorarioUpdateComponent } from './horario-update.component';

@Injectable({ providedIn: 'root' })
export class HorarioResolve implements Resolve<IHorario> {
  constructor(private service: HorarioService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHorario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((horario: HttpResponse<Horario>) => {
          if (horario.body) {
            return of(horario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Horario());
  }
}

export const horarioRoute: Routes = [
  {
    path: '',
    component: HorarioComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.horario.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HorarioDetailComponent,
    resolve: {
      horario: HorarioResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.horario.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HorarioUpdateComponent,
    resolve: {
      horario: HorarioResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.horario.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HorarioUpdateComponent,
    resolve: {
      horario: HorarioResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.horario.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
