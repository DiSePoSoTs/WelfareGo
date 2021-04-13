/* ASSISTENZA ECONOMICA */
/*
'EC001'
'EC002'
'EC003'
'EC004'
'EC005'
'AD013'
'EC100'
'MI101'
*/
/****** cittadino residente *************/
/* NUMERO DOMANDE PRESENTATE */
select count(*) from (
SELECT anagrafe_soc.cod_fisc                                          AS cf,
  pi.cod_pai                                                          AS codpai,
  pi.cod_tipint                                                       AS codtipint,
  to_number(ROUND(months_between(sysdate, anagrafe_soc.dt_nasc) /12)) AS eta
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
AND pi.cod_tipint                                               IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
AND l.cod_stato    = '100'
)

/* NUMERO DOMANDE ACCOLTE */
select count(*) from (
SELECT anagrafe_soc.cod_fisc                                          AS cf,
  pi.cod_pai                                                          AS codpai,
  pi.cod_tipint                                                       AS codtipint,
  to_number(ROUND(months_between(sysdate, anagrafe_soc.dt_nasc) /12)) AS eta
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
AND (pi.cod_tipint                                               IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
AND dt_esec is not null
AND l.cod_stato    = '100')
)

/* numero beneficiari */
select count(distinct cf) from (
SELECT anagrafe_soc.cod_fisc                                          AS cf,
  pi.cod_pai                                                          AS codpai,
  pi.cod_tipint                                                       AS codtipint,
  to_number(ROUND(months_between(sysdate, anagrafe_soc.dt_nasc) /12)) AS eta
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
AND (pi.cod_tipint                                               IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
AND l.cod_stato    = '100'
/*AND dt_esec is not null)*/
)
)

/* IMPORTO EROGATO TOTALE */

select sum(consuntivo) from (
SELECT pim.bdg_cons_eur as consuntivo
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
JOIN pai_intervento_mese pim on (pim.cod_pai = pi.cod_pai and pim.cod_tipint = pi.cod_tipint and pim.cnt_tipint = pi.cnt_tipint)
WHERE (pim.cod_tipint                                               IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
and pim.anno_eff=2016
AND l.cod_stato='100')

/*AND dt_esec is not null)*/
)

/* nuclei assistiti */

SELECT COUNT(*) FROM
  (SELECT anagrafe_fam.cod_ana
  FROM anagrafe_fam
  WHERE anagrafe_fam.cod_ana_fam IN
    ( SELECT DISTINCT(anagrafe_soc.cod_ana) AS assistiti
    FROM anagrafe_soc
    JOIN pai
    ON pai.cod_ana=anagrafe_soc.cod_ana
    JOIN pai_intervento pi
    ON pai.cod_pai = pi.cod_pai
    JOIN parametri_indata
    ON pai.id_param_uot=parametri_indata.id_param_indata
    JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
    WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
    OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
    OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
    AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
    OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
    AND (pi.cod_tipint                                              IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
    )
    AND l.cod_stato='100'
    )
    GROUP BY anagrafe_fam.cod_ana
    ORDER BY anagrafe_fam.cod_ana
    )


/****** cittadino non residente *************/
/* NUMERO DOMANDE PRESENTATE */
select count(*) from (
SELECT anagrafe_soc.cod_fisc                                          AS cf,
  pi.cod_pai                                                          AS codpai,
  pi.cod_tipint                                                       AS codtipint,
  to_number(ROUND(months_between(sysdate, anagrafe_soc.dt_nasc) /12)) AS eta
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
AND pi.cod_tipint                                               IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
AND l.cod_stato    <> '100'
)

/* NUMERO DOMANDE ACCOLTE */
select count(*) from (
SELECT anagrafe_soc.cod_fisc                                          AS cf,
  pi.cod_pai                                                          AS codpai,
  pi.cod_tipint                                                       AS codtipint,
  to_number(ROUND(months_between(sysdate, anagrafe_soc.dt_nasc) /12)) AS eta
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
AND (pi.cod_tipint                                               IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
AND dt_esec is not null
AND l.cod_stato    <> '100')
)

/* numero beneficiari */
select count(distinct cf) from (
SELECT anagrafe_soc.cod_fisc                                          AS cf,
  pi.cod_pai                                                          AS codpai,
  pi.cod_tipint                                                       AS codtipint,
  to_number(ROUND(months_between(sysdate, anagrafe_soc.dt_nasc) /12)) AS eta
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
AND (pi.cod_tipint                                               IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
AND l.cod_stato    <> '100'
/*AND dt_esec is not null)*/
)
)

/* IMPORTO EROGATO TOTALE */

select sum(consuntivo) from (
SELECT pim.bdg_cons_eur as consuntivo
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
JOIN pai_intervento_mese pim on (pim.cod_pai = pi.cod_pai and pim.cod_tipint = pi.cod_tipint and pim.cnt_tipint = pi.cnt_tipint)
WHERE (pim.cod_tipint                                               IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
and pim.anno_eff=2016
AND l.cod_stato<>'100')

/*AND dt_esec is not null)*/
)

/* nuclei assistiti */

SELECT COUNT(*) FROM
  (SELECT anagrafe_fam.cod_ana
  FROM anagrafe_fam
  WHERE anagrafe_fam.cod_ana_fam IN
    ( SELECT DISTINCT(anagrafe_soc.cod_ana) AS assistiti
    FROM anagrafe_soc
    JOIN pai
    ON pai.cod_ana=anagrafe_soc.cod_ana
    JOIN pai_intervento pi
    ON pai.cod_pai = pi.cod_pai
    JOIN parametri_indata
    ON pai.id_param_uot=parametri_indata.id_param_indata
    JOIN luogo l
ON anagrafe_soc.cod_luogo_nasc = l.cod_luogo
    WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
    OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
    OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
    AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
    OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
    AND (pi.cod_tipint                                              IN ('EC001','EC002','EC003','EC004','EC005'/*,'MI101'*/,'AD009')
    )
    AND l.cod_stato<>'100'
    )
    GROUP BY anagrafe_fam.cod_ana
    ORDER BY anagrafe_fam.cod_ana
    )


/**
*** dettagli per tipologia di contributo
**/

/* NUMERO DI CONTRIBUTI EROGATI */

select count(*) from (
SELECT anagrafe_soc.cod_fisc                                          AS cf,
  pi.cod_pai                                                          AS codpai,
  pi.cod_tipint                                                       AS codtipint,
  to_number(ROUND(months_between(sysdate, anagrafe_soc.dt_nasc) /12)) AS eta
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
WHERE ( (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)-1) BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR (pi.dt_avvio BETWEEN to_date('01/01/2016','dd/mm/yyyy') AND to_date('31/12/2016','dd/mm/yyyy'))
OR(pi.dt_avvio                                                   < to_date('01/01/2016','dd/mm/yyyy'))
AND (NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)      -1)  >to_date('31/12/2016','dd/mm/yyyy')
OR NVL (pi.dt_chius, add_months(pi.dt_avvio, pi.dur_mesi)        -1) IS NULL))
AND pi.cod_tipint                                               IN (/*'EC001','EC002','EC002',*/'EC004','EC005'/*,'MI101','AD009'*/)
AND pi.dt_esec is not null)

/* IMPORTO EROGATO TOTALE */

select sum(consuntivo) from (
SELECT pim.bdg_cons_eur as consuntivo
FROM anagrafe_soc
JOIN pai
ON pai.cod_ana = anagrafe_soc.cod_ana
JOIN pai_intervento pi
ON pi.cod_pai = pai.cod_pai
JOIN pai_intervento_mese pim on (pim.cod_pai = pi.cod_pai and pim.cod_tipint = pi.cod_tipint and pim.cnt_tipint = pi.cnt_tipint)
WHERE (pim.cod_tipint                                               IN (/*'EC001','EC002','EC002',*/'EC004','EC005'/*,'MI101','AD009'*/)
and pim.anno_eff=2016)
)
