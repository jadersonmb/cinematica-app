import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMedico, Medico } from 'app/shared/model/medico.model';
import { MedicoService } from './medico.service';
import { MedicoComponent } from './medico.component';
import { MedicoDetailComponent } from './medico-detail.component';
import { MedicoUpdateComponent } from './medico-update.component';

@Injectable({ providedIn: 'root' })
export class MedicoResolve implements Resolve<IMedico> {
  constructor(private service: MedicoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedico> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((medico: HttpResponse<Medico>) => {
          if (medico.body) {
            return of(medico.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Medico());
  }
}

export const medicoRoute: Routes = [
  {
    path: '',
    component: MedicoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.medico.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MedicoDetailComponent,
    resolve: {
      medico: MedicoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.medico.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MedicoUpdateComponent,
    resolve: {
      medico: MedicoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.medico.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MedicoUpdateComponent,
    resolve: {
      medico: MedicoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.medico.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
