package testes.funcionario;

import model.rn.FuncionarioRN;
import model.vo.BairroVO;
import model.vo.CargoVO;
import model.vo.CidadeVO;
import model.vo.EndPostalVO;
import model.vo.EnderecoVO;
import model.vo.EstadoVO;
import model.vo.FuncionarioVO;
import model.vo.LogradouroVO;
import model.vo.SexoVO;
import model.vo.TelefoneVO;
import model.vo.TipoPessoaVO;

import java.time.LocalDate;
import java.util.List;

public class InserirFuncionarioCompletoTESTE {

    public static void main(String[] args) {
        System.out.println("=== Teste salvar funcionario completo ===");
        try {
            FuncionarioVO funcionario = criarFuncionarioFake();
            FuncionarioRN rn = new FuncionarioRN();
            rn.salvarNovo(funcionario);
            System.out.println("Funcionário salvo com sucesso!");
        } catch (Exception e) {
            System.err.println("Falha ao salvar funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static FuncionarioVO criarFuncionarioFake() {
        FuncionarioVO vo = new FuncionarioVO();
        vo.setPes_cpf("12345678902");
        vo.setPes_nome("Funcionario Teste2");
        vo.setPes_email("funcionario@teste.com");
        vo.setPes_dt_nascimento(LocalDate.of(1990, 1, 10));
        vo.setPes_ativo(true);

        SexoVO sexo = new SexoVO();
        sexo.setSex_id(1);
        vo.setPes_sexo(sexo);

        TipoPessoaVO tipo = new TipoPessoaVO();
        tipo.setTipo_pessoa_id(1);
        tipo.setCodigo("F");
        vo.setPes_tipo_pessoa(tipo);

        TelefoneVO telefone = new TelefoneVO();
        telefone.setTel_codPais("55");
        telefone.setTel_ddd("11");
        telefone.setTel_numero("999999999");
        vo.setTelefone(List.of(telefone));

        EndPostalVO endPostal = new EndPostalVO();
        endPostal.setEndP_id(1); // assumir que já existe no banco

        LogradouroVO logradouro = new LogradouroVO();
        logradouro.setLogradouro_id(1);
        endPostal.setEndP_logradouro(logradouro);

        CidadeVO cidade = new CidadeVO();
        cidade.setCid_id(1);
        endPostal.setEndP_cidade(cidade);

        BairroVO bairro = new BairroVO();
        bairro.setBairro_id(1);
        endPostal.setEndP_bairro(bairro);

        EstadoVO estado = new EstadoVO();
        estado.setEst_sigla("SP");
        endPostal.setEndP_estado(estado);
        endPostal.setEndP_nomeRua("Rua cadastrada2");
        endPostal.setEndP_cep("12345000");

        EnderecoVO endereco = new EnderecoVO();
        endereco.setEnd_endP_id(endPostal);
        endereco.setEnd_numero("100");
        endereco.setEnd_complemento("Sala 1");
        vo.setEndereco(List.of(endereco));

        CargoVO cargo = new CargoVO();
        cargo.setCar_id(1);
        vo.setFnc_cargo(cargo);

        vo.setFnc_numPis("12345678901");
        vo.setFnc_dtContratacao(LocalDate.now());
        vo.setFnc_salario(3500.00);
        vo.setFnc_dtDemissao(null);
        vo.setFnc_motivo_demissao(null);

        return vo;
    }
}
