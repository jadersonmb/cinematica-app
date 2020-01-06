import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEspecialidade, Especialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from './especialidade.service';
import { EspecialidadeComponent } from './especialidade.component';
import { EspecialidadeDetailComponent } from './especialidade-detail.component';
import { EspecialidadeUpdateComponent } from './especialidade-update.component';

@Injectable({ providedIn: 'root' })
export class EspecialidadeResolve implements Resolve<IEspecialidade> {
  constructor(private service: EspecialidadeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEspecialidade> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((especialidade: HttpResponse<Especialidade>) => {
          if (especialidade.body) {
            return of(especialidade.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Especialidade());
  }
}

export const especialidadeRoute: Routes = [
  {
    path: '',
    component: EspecialidadeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.especialidade.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EspecialidadeDetailComponent,
    resolve: {
      especialidade: EspecialidadeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.especialidade.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EspecialidadeUpdateComponent,
    resolve: {
      especialidade: EspecialidadeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.especialidade.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EspecialidadeUpdateComponent,
    resolve: {
      especialidade: EspecialidadeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.especialidade.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
