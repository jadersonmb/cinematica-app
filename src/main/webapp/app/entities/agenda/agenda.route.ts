import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAgenda, Agenda } from 'app/shared/model/agenda.model';
import { AgendaService } from './agenda.service';
import { AgendaComponent } from './agenda.component';
import { AgendaDetailComponent } from './agenda-detail.component';
import { AgendaUpdateComponent } from './agenda-update.component';

@Injectable({ providedIn: 'root' })
export class AgendaResolve implements Resolve<IAgenda> {
  constructor(private service: AgendaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAgenda> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((agenda: HttpResponse<Agenda>) => {
          if (agenda.body) {
            return of(agenda.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Agenda());
  }
}

export const agendaRoute: Routes = [
  {
    path: '',
    component: AgendaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.agenda.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AgendaDetailComponent,
    resolve: {
      agenda: AgendaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.agenda.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AgendaUpdateComponent,
    resolve: {
      agenda: AgendaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.agenda.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AgendaUpdateComponent,
    resolve: {
      agenda: AgendaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.agenda.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
