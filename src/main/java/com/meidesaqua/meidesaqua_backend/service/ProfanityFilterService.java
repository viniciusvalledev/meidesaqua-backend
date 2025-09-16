package com.meidesaqua.meidesaqua_backend.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfanityFilterService {

    // A lista foi expandida para incluir variações com números (leetspeak)
    private static final List<String> PALAVRAS_PROIBIDAS = Arrays.asList(
            // COLE A LISTA COMPLETA E ATUALIZADA DA PARTE 2 AQUI
            //--- INÍCIO DA LISTA FINAL ---
// PALAVRAS-RAIZ E VARIAÇÕES COMUNS
            "anal", "anus", "arrombada", "arrombado", "babaovo", "babaca", "bagos", "baitola", "bicha", "bixa",
            "boazuda", "boceta", "boiola", "bolagato", "boquete", "bosta", "brioco", "bronha", "buceta", "bunda",
            "bundao", "bundudo", "burra", "burro", "busseta", "cabaco", "cabrao", "cagar", "cagado", "cagao",
            "canalha", "caralho", "krl", "cacete", "kct", "corna", "corno", "cornudo", "chereca", "cherereca",
            "chifruda", "chifrudo", "chota", "chupa", "chupada", "chupado", "clitoris", "cocaina", "coco", "cu",
            "cuzinho", "cuzao", "desgraca", "drogado", "energumeno", "enfia", "estupida", "estupidez", "estupido",
            "estupro", "fiofo", "foda", "fodendo", "foder", "fodase", "fodeu", "fodida", "fodido", "fornicar",
            "fudendo", "fuder", "fudida", "fudido", "furo", "furona", "furnicar", "gaiato", "gay", "gonorreia",
            "grelinho", "grelo", "gozada", "gozado", "gozar", "herege", "idiota", "idiotice", "imbecil", "iscroto",
            "ladrao", "lambe", "lesbica", "macaca", "macaco", "maconha", "masturba", "masturbacao", "merda",
            "merdinha", "mija", "mijada", "mijado", "mijo", "mocreia", "mongol", "nadegas", "paspalhao", "peido",
            "pemba", "penis", "pentelha", "pentelho", "perereca", "peru", "pica", "picao", "pila", "pinto",
            "pintudo", "piranha", "piroca", "piru", "porno", "porra", "prega", "prostituta", "prostituto", "punheta",
            "punheteiro", "pustula", "puta", "puto", "puxasaco", "pqp", "putaquepariu", "rabo", "rabudo", "rabuda",
            "racha", "retardada", "retardado", "rola", "rosca", "sapatao", "siririca", "tarada", "tarado", "testuda",
            "tesuda", "tezuda", "transar", "trocha", "troucha", "trouxa", "troxa", "trolha", "vaca", "vadia",
            "vagabunda", "vagabundo", "vagina", "veada", "veado", "viada", "viado", "xana", "xaninha", "xavasca",
            "xereca", "xexeca", "xochota", "xota", "xoxota", "fdp", "filadaputa", "vsf", "vaisifoder", "tnc",
            "tomarnocu", "otario", "otaria", "cretino", "cretina", "vigarista", "pilantra", "escroto", "escrota",
            "ridiculo", "ridicula", "nojento", "nojenta", "lixo", "verme",
// ABREVIAÇÕES E GÍRIAS
            "prr", "krlh", "mlk", "sfd", "slc", "pprt", "vtc", "vtmnc",
            "fdc", "tmnc", "mrd", "tmr", "vtnc", "vtmn", "nfd", "pqpp", "fpa", "crlh", "crl",
            "kcete", "bglh", "bct", "bctx", "vx", "xot4", "xoxota", "fod4", "fodase", "fud3", "fud3u",
            "f0d4", "f0d@s3", "vag4", "p1ca", "c0c0", "b0st@",
// VARIAÇÕES COM NÚMEROS (LEETSPEAK)
            "p0rra", "p0ha", "f0der", "fud3r", "fod3r", "crlh0", "c4ralho", "arromb4do", "b0ceta", "viad0",
            "put0", "merd4", "cuz4o", "b0sta", "vag4bundo", "vag4bunda",
// PALAVRÕES ADICIONAIS
            "abestado", "anta", "asinina", "asinino", "babona", "burraldo", "bundinha", "corno manso",
            "debiloide", "demoniado", "desgraçado", "desgraçada", "estrupício", "escarrado", "estrume",
            "esporrada", "esporro", "estoura-toba", "fela", "feladaputa", "filha da mãe", "filho de uma égua",
            "franga", "frango", "idiotona", "jegue", "jumento", "lambedor", "maria chuteira", "maria gasolina",
            "maria mijona", "merdel", "merdeiro", "panaca", "pangaré", "pau no cu", "pau no seu", "pauzudo",
            "peidorreiro", "porcaria", "pústula", "rabeta", "ralé", "safada", "safado", "sem noção", "sem vergonha",
            "tapado", "tapada", "traste", "urubu", "vagaba", "vagabundagem", "veaca", "zé buceta", "zé ruela", "zé povinho"
    );


    /**
     * Verifica se o texto contém palavras proibidas após normalizá-lo.
     * @param texto O texto original a ser verificado.
     * @return true se contiver uma palavra proibida, false caso contrário.
     */
    public boolean contemPalavrao(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }

        // 1. Normaliza o texto de entrada para uma versão "limpa"
        String textoNormalizado = normalizarTexto(texto);

        // 2. Verifica se a versão limpa contém alguma das palavras proibidas
        return PALAVRAS_PROIBIDAS.stream().anyMatch(textoNormalizado::contains);
    }

    /**
     * Normaliza o texto de forma mais segura.
     * - Converte para minúsculas.
     * - Remove todos os caracteres que não sejam letras ou números.
     * (Isso impede que "nota 1000" seja transformado em "notaiooo")
     * @param input O texto a ser normalizado.
     * @return O texto normalizado.
     */
    private String normalizarTexto(String input) {
        if (input == null) {
            return "";
        }
        String texto = input.toLowerCase();

        // Remove todos os caracteres que não sejam letras ou números (a-z, 0-9)
        texto = texto.replaceAll("[^a-zA-Z0-9]", "");

        return texto;
    }
}