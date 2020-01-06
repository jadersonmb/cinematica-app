import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { ConfiguracaoAgendaDetailComponent } from 'app/entities/configuracao-agenda/configuracao-agenda-detail.component';
import { ConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';

describe('Component Tests', () => {
  describe('ConfiguracaoAgenda Management Detail Component', () => {
    let comp: ConfiguracaoAgendaDetailComponent;
    let fixture: ComponentFixture<ConfiguracaoAgendaDetailComponent>;
    const route = ({ data: of({ configuracaoAgenda: new ConfiguracaoAgenda(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [ConfiguracaoAgendaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ConfiguracaoAgendaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfiguracaoAgendaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load configuracaoAgenda on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.configuracaoAgenda).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
