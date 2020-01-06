import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IConfiguracaoAgenda, ConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';
import { ConfiguracaoAgendaService } from './configuracao-agenda.service';
import { ConfiguracaoAgendaComponent } from './configuracao-agenda.component';
import { ConfiguracaoAgendaDetailComponent } from './configuracao-agenda-detail.component';
import { ConfiguracaoAgendaUpdateComponent } from './configuracao-agenda-update.component';

@Injectable({ providedIn: 'root' })
export class ConfiguracaoAgendaResolve implements Resolve<IConfiguracaoAgenda> {
  constructor(private service: ConfiguracaoAgendaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConfiguracaoAgenda> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((configuracaoAgenda: HttpResponse<ConfiguracaoAgenda>) => {
          if (configuracaoAgenda.body) {
            return of(configuracaoAgenda.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConfiguracaoAgenda());
  }
}

export const configuracaoAgendaRoute: Routes = [
  {
    path: '',
    component: ConfiguracaoAgendaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.configuracaoAgenda.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ConfiguracaoAgendaDetailComponent,
    resolve: {
      configuracaoAgenda: ConfiguracaoAgendaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.configuracaoAgenda.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ConfiguracaoAgendaUpdateComponent,
    resolve: {
      configuracaoAgenda: ConfiguracaoAgendaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.configuracaoAgenda.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ConfiguracaoAgendaUpdateComponent,
    resolve: {
      configuracaoAgenda: ConfiguracaoAgendaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cinematicaApp.configuracaoAgenda.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
