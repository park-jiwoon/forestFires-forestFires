package com.ZeroWaveV2.FireAlertV2.config;

import com.ZeroWaveV2.FireAlertV2.model.Mountain_recommend;
import com.ZeroWaveV2.FireAlertV2.repository.MountainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MountainInitializer implements CommandLineRunner {

    @Autowired
    private MountainRepository mountainRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Mountain_recommend> mountains = Arrays.asList(
                new Mountain_recommend("가리산","강원도",1050,"여름","3시간30분~4시간미만","소양호에서 폭포 따라 정상까지","src\\main\\resources\\static\\mountain_images\\mt_01.jpg"),
        		new Mountain_recommend("가리왕산","강원도",1561,"여름/겨울","4시간~4시간30분미만","약초 많은 태백산의 지붕","src\\main\\resources\\static\\mountain_images\\mt_02.jpg"),
        		new Mountain_recommend("가야산","경상북도",1432,"가을","4시간~4시간30분미만","연꽃으로 피어오른 불교의 성지","src\\main\\resources\\static\\mountain_images\\mt_03.jpg"),
        		new Mountain_recommend("가지산","경상남도",1240,"가을","3시간~3시간30분미만","영남알프스의 최고봉","src\\main\\resources\\static\\mountain_images\\mt_04.jpg"),
        		new Mountain_recommend("감악산","경기도",674,"여름","3시간~3시간30분미만","의적 임꺽정이 쉬어 가던 산","src\\main\\resources\\static\\mountain_images\\mt_05.jpg"),
        		new Mountain_recommend("강천산","전라북도",585,"봄/가을","2시간~2시간30분미만","국내 최초의 군립공원","src\\main\\resources\\static\\mountain_images\\mt_06.jpg"),
        		new Mountain_recommend("계룡산","충청남도",846,"가을","5시간이상","정감록에서 예언한 왕도가 들어설 산","src\\main\\resources\\static\\mountain_images\\mt_07.jpg"),
        		new Mountain_recommend("계방산","강원도",1579,"가을/겨울","3시간30분~4시간미만","해발은 높되 산세는 유순하니","src\\main\\resources\\static\\mountain_images\\mt_08.jpg"),
        		new Mountain_recommend("공작산","강원도",887,"가을/겨울","3시간~3시간30분미만","암봉과 노송이 한폭 동양화로 걸려 있어","src\\main\\resources\\static\\mountain_images\\mt_09.jpg"),
        		new Mountain_recommend("관악산","서울시",632,"봄/여름","3시간~3시간30분미만","수차례 화마가 쓸고 갔던 불의 산","src\\main\\resources\\static\\mountain_images\\mt_10.jpg"),
        		new Mountain_recommend("구병산","경상북도",876,"여름/가을","5시간이상","아홉 개 봉우리가 병풍처럼","src\\main\\resources\\static\\mountain_images\\mt_11.jpg"),
        		new Mountain_recommend("금산","경상남도",704,"봄/가을","3시간~3시간30분미만","이성계가 비단으로 덮겠다던 산","src\\main\\resources\\static\\mountain_images\\mt_12.jpg"),
        		new Mountain_recommend("금수산","충청북도",1015,"봄/여름","3시간30분~4시간미만","눈 시린 가을 비단산","src\\main\\resources\\static\\mountain_images\\mt_13.jpg"),
        		new Mountain_recommend("금오산","경상북도",976,"여름","3시간30분~4시간미만","마애보살입상이 지키는 남숭산","src\\main\\resources\\static\\mountain_images\\mt_14.jpg"),
        		new Mountain_recommend("금정산","부산시",800,"겨울","2시간~2시간30분미만","부산 시민의 1일 등산코스","src\\main\\resources\\static\\mountain_images\\mt_15.jpg"),
        		new Mountain_recommend("깃대봉","전라남도",367,"봄","2시간~2시간30분미만","덩굴사철, 식나무 및 동백림 등이 자생하는 천연보호구역","src\\main\\resources\\static\\mountain_images\\mt_16.jpg"),
        		new Mountain_recommend("남산(금오산)","경상북도",495,"겨울","3시간30분~4시간미만","신라의 천년 역사가 숨쉬는 산","src\\main\\resources\\static\\mountain_images\\mt_17.jpg"),
        		new Mountain_recommend("내연산","경상북도",711,"여름","2시간~2시간30분미만","신비의 거울을 묻은 흙산","src\\main\\resources\\static\\mountain_images\\mt_18.jpg"),
        		new Mountain_recommend("내장산(신선봉)","전라북도",763,"가을","2시간~2시간30분미만","불타는 호남의 금강","src\\main\\resources\\static\\mountain_images\\mt_19.jpg"),
        		new Mountain_recommend("대둔산","충청남도",879,"여름/가을","3시간30분~4시간미만","원효대사가 춤 추던 천하의 승지","src\\main\\resources\\static\\mountain_images\\mt_20.jpg"),
        		new Mountain_recommend("대암산","강원도",1312,"여름/가을","3시간~3시간30분미만","우리나라 최대 희귀생물자원의 보고","src\\main\\resources\\static\\mountain_images\\mt_21.jpg"),
        		new Mountain_recommend("대야산","경상북도",931,"여름","5시간이상","신선 노닐던 영험한 산 (일명 대하산)","src\\main\\resources\\static\\mountain_images\\mt_22.jpg"),
        		new Mountain_recommend("덕숭산(수덕산)","충청남도",495,"겨울","2시간~2시간30분미만","품은 작아도 도량은 넓어","src\\main\\resources\\static\\mountain_images\\mt_23.jpg"),
        		new Mountain_recommend("덕유산(향적봉)","전라북도",1614,"여름","4시간~4시간30분미만","첩첩산중, 덕이 겹친 큰 산","src\\main\\resources\\static\\mountain_images\\mt_24.jpg"),
        		new Mountain_recommend("덕항산","강원도",1072,"가을","5시간이상","태고적 신비를 간직한 환선굴속으로","src\\main\\resources\\static\\mountain_images\\mt_25.jpg"),
        		new Mountain_recommend("도락산","충청북도",965,"여름/가을","4시간~4시간30분미만","도를 깨닫고 스스로 즐길만한 산","src\\main\\resources\\static\\mountain_images\\mt_26.jpg"),
        		new Mountain_recommend("도봉산(자운봉)","서울시",740,"여름/가을","3시간30분~4시간미만","거미줄처럼 얽힌 미로의 산","src\\main\\resources\\static\\mountain_images\\mt_27.jpg"),
        		new Mountain_recommend("두륜산","전라남도",700,"가을/겨울","4시간~4시간30분미만","삼재가 들지 않는 천하의 요새","src\\main\\resources\\static\\mountain_images\\mt_28.jpg"),
        		new Mountain_recommend("두타산","강원도",1357,"가을","5시간이상","번뇌를 잠 재우는 무릉도원","src\\main\\resources\\static\\mountain_images\\mt_29.jpg"),
        		new Mountain_recommend("마니산","인천시",472,"봄","3시간~3시간30분미만","역대 왕들이 하늘제를 올리던 산","src\\main\\resources\\static\\mountain_images\\mt_30.jpg"),
        		new Mountain_recommend("마이산","전라북도",687,"여름/가을","2시간~2시간30분미만","음양의 궁합을 맞춘 암수봉우리","src\\main\\resources\\static\\mountain_images\\mt_31.jpg"),
        		new Mountain_recommend("명성산","강원도",922,"여름/가을","3시간30분~4시간미만","마의태자를 따라 울었던 울음산","src\\main\\resources\\static\\mountain_images\\mt_32.jpg"),
        		new Mountain_recommend("명지산","경기도",1252,"여름/가을","5시간이상","사방으로 산자락을 펼친 명지산","src\\main\\resources\\static\\mountain_images\\mt_33.jpg"),
        		new Mountain_recommend("모악산","전라북도",795,"봄","3시간30분~4시간미만","호남사경의 하나","src\\main\\resources\\static\\mountain_images\\mt_34.jpg"),
        		new Mountain_recommend("무등산","광주시",1186,"겨울","5시간이상","빛고을 광주의 얼굴","src\\main\\resources\\static\\mountain_images\\mt_35.jpg"),
        		new Mountain_recommend("무학산","경상남도",761,"가을","3시간30분~4시간미만","산세를 보아하니 춤추는 학 같아","src\\main\\resources\\static\\mountain_images\\mt_36.jpg"),
        		new Mountain_recommend("미륵산","경상남도",458,"봄/여름","2시간30분~3시간미만","봉화대 자취 간직한 미륵신앙의 본산","src\\main\\resources\\static\\mountain_images\\mt_37.jpg"),
        		new Mountain_recommend("민주지산","충청북도",1241,"여름/가을","4시간30분~5시간미만","화전민터가 있는 원시숲","src\\main\\resources\\static\\mountain_images\\mt_38.jpg"),
        		new Mountain_recommend("방장산","전라남도",733,"봄/여름","5시간이상","나그네의 봇짐을 털던 도적떼의 은둔지","src\\main\\resources\\static\\mountain_images\\mt_39.jpg"),
        		new Mountain_recommend("방태산(주억봉)","강원도",1445,"여름/가을","5시간이상","깊은 산속 옹달샘","src\\main\\resources\\static\\mountain_images\\mt_40.jpg"),
        		new Mountain_recommend("백덕산","강원도",1350,"겨울","4시간30분~5시간미만","주능선에 피어나는 설화","src\\main\\resources\\static\\mountain_images\\mt_41.jpg"),
        		new Mountain_recommend("백암산","전라북도",741,"봄/가을","3시간30분~4시간미만","산하면 내장, 고적하면 백암","src\\main\\resources\\static\\mountain_images\\mt_42.jpg"),
        		new Mountain_recommend("백운산(포천)","경기도",903,"여름","2시간30분~3시간미만","경기도와 강원도의 경계, 카라멜고개","src\\main\\resources\\static\\mountain_images\\mt_43.jpg"),
        		new Mountain_recommend("백운산(광양)","전라남도",1222,"여름/겨울","4시간~4시간30분미만","또아리봉에서 바라본 낙조","src\\main\\resources\\static\\mountain_images\\mt_44.jpg"),
        		new Mountain_recommend("백운산(정선)","강원도",883,"여름/가을","5시간이상","태고의 신비를 간직한 천혜의 비경","src\\main\\resources\\static\\mountain_images\\mt_45.jpg"),
        		new Mountain_recommend("변산(의상봉)","전라북도",459,"여름","3시간30분~4시간미만","호남정맥에서 독립된 산군 형성","src\\main\\resources\\static\\mountain_images\\mt_46.jpg"),
        		new Mountain_recommend("북한산(백운대)","서울시",835,"봄","4시간30분~5시간미만","가거라 삼각산아 다시 보자 한강수야","src\\main\\resources\\static\\mountain_images\\mt_47.jpg"),
        		new Mountain_recommend("비슬산(천왕봉)","대구시",1083,"봄/가을","4시간30분~5시간미만","대구 남쪽에 품 넓게 우뚝 솟은 산","src\\main\\resources\\static\\mountain_images\\mt_48.jpg"),
        		new Mountain_recommend("삼악산","강원도",655,"여름","2시간30분~3시간미만","물, 나무, 골짜기가 연출하는 풍류의 산","src\\main\\resources\\static\\mountain_images\\mt_49.jpg"),
        		new Mountain_recommend("서대산","충청남도",905,"여름","4시간~4시간30분미만","배필을 찾아주는 직녀탄금대 약수","src\\main\\resources\\static\\mountain_images\\mt_50.jpg"),
        		new Mountain_recommend("선운산","전라북도",334,"가을","3시간~3시간30분미만","구름속에 선을 닦는 동백산","src\\main\\resources\\static\\mountain_images\\mt_51.jpg"),
        		new Mountain_recommend("설악산(대청봉)","강원도",1708,"여름/가을","5시간이상","섬세한 비경 두루 갖춘 팔방미인","src\\main\\resources\\static\\mountain_images\\mt_52.jpg"),
        		new Mountain_recommend("성인봉","경상북도",986,"여름/가을","5시간이상","풍랑을 이겨낸 겨레의 기상","src\\main\\resources\\static\\mountain_images\\mt_53.jpg"),
        		new Mountain_recommend("소백산","경상북도",1439,"봄/겨울","5시간이상","퇴계 이황이 시 쓰던 산","src\\main\\resources\\static\\mountain_images\\mt_54.jpg"),
        		new Mountain_recommend("소요산","경기도",587,"봄/가을","3시간30분~4시간미만","말발굽 모양을 한 경기도의 소금강","src\\main\\resources\\static\\mountain_images\\mt_55.jpg"),
        		new Mountain_recommend("속리산","경상북도",1058,"여름/겨울","5시간이상","속세를 등진 이들의 회심처","src\\main\\resources\\static\\mountain_images\\mt_56.jpg"),
        		new Mountain_recommend("신불산","울산시",1159,"가을","5시간이상","광활한 억새밭이 펼쳐진 영남 알프스","src\\main\\resources\\static\\mountain_images\\mt_57.jpg"),
        		new Mountain_recommend("연화산","경상남도",524,"봄/가을","2시간30분~3시간미만","병을 고치는 신비의 옥천샘","src\\main\\resources\\static\\mountain_images\\mt_58.jpg"),
        		new Mountain_recommend("오대산(비로봉)","강원도",1565,"여름/가을","4시간~4시간30분미만","한 승려의 희생으로 소실위기를 모면한 상원사","src\\main\\resources\\static\\mountain_images\\mt_59.jpg"),
        		new Mountain_recommend("오봉산","강원도",777,"여름","4시간30분~5시간미만","동양화속에 다섯 개 바위","src\\main\\resources\\static\\mountain_images\\mt_60.jpg"),
        		new Mountain_recommend("용문산","경기도",1157,"여름","5시간이상","조선 개국으로 다시 태어난 미지산","src\\main\\resources\\static\\mountain_images\\mt_61.jpg"),
        		new Mountain_recommend("용화산","강원도",877,"여름","3시간~3시간30분미만","이야기가 있는 바위 전시장","src\\main\\resources\\static\\mountain_images\\mt_62.jpg"),
        		new Mountain_recommend("운문산","경상북도",1195,"가을/겨울","5시간이상","숨겨진 비경을 들킬세라","src\\main\\resources\\static\\mountain_images\\mt_63.jpg"),
        		new Mountain_recommend("운악산(현등산)","경기도",934,"봄/가을","3시간30분~4시간미만","구름을 뚫고 솟은 경기의 소금강","src\\main\\resources\\static\\mountain_images\\mt_64.jpg"),
        		new Mountain_recommend("운장산","전라북도",1125,"여름","5시간이상","금강과 만경강의 발원지","src\\main\\resources\\static\\mountain_images\\mt_65.jpg"),
        		new Mountain_recommend("월악산","충청북도",1095,"여름/가을","5시간이상","병화가 미치지 않는 심산유곡","src\\main\\resources\\static\\mountain_images\\mt_66.jpg"),
        		new Mountain_recommend("월출산","전라남도",810,"봄/가을","3시간30분~4시간미만","웅장하고 섬세한 기암괴석 진열대","src\\main\\resources\\static\\mountain_images\\mt_67.jpg"),
        		new Mountain_recommend("유명산","경기도",864,"여름/가을","3시간30분~4시간미만","기암괴석과 하늘을 덮는 수풀림의 조화","src\\main\\resources\\static\\mountain_images\\mt_68.jpg"),
        		new Mountain_recommend("응봉산(매봉산)","강원도",999,"여름","5시간이상","헤아릴수 없는 계곡 줄줄이 이어져","src\\main\\resources\\static\\mountain_images\\mt_69.jpg"),
        		new Mountain_recommend("장안산","전라북도",1237,"가을","4시간30분~5시간미만","광활한 갈대밭과 덕산용소계곡","src\\main\\resources\\static\\mountain_images\\mt_70.jpg"),
        		new Mountain_recommend("재약산","경상남도",1119,"가을","4시간30분~5시간미만","하늘거리는 억새 품에 나를 맡긴다","src\\main\\resources\\static\\mountain_images\\mt_71.jpg"),
        		new Mountain_recommend("적상산","전라북도",1030,"여름/가을","4시간~4시간30분미만","단풍이 아름다운 산","src\\main\\resources\\static\\mountain_images\\mt_72.jpg"),
        		new Mountain_recommend("점봉산","강원도",1426,"봄/여름","5시간이상","원시림과 계곡이 조화롭게 어우러진 산","src\\main\\resources\\static\\mountain_images\\mt_73.jpg"),
        		new Mountain_recommend("조계산","전라남도",887,"여름","5시간이상","산세가 부드러운 산","src\\main\\resources\\static\\mountain_images\\mt_74.jpg"),
        		new Mountain_recommend("주왕산","경상북도",722,"여름/가을","3시간30분~4시간미만","후주천왕의 은둔지","src\\main\\resources\\static\\mountain_images\\mt_75.jpg"),
        		new Mountain_recommend("주흘산","경상북도",1108,"여름/가을","4시간~4시간30분미만","삼각산과 다툴 만큼 빼어난 명산","src\\main\\resources\\static\\mountain_images\\mt_76.jpg"),
        		new Mountain_recommend("지리산(천왕봉)","전라북도",1915,"봄/여름","5시간이상","백두의 끝, 역사의 피와 눈물이 함께 묻혀있는 이상향","src\\main\\resources\\static\\mountain_images\\mt_77.jpg"),
        		new Mountain_recommend("지리산","경상남도",399,"봄","5시간이상","경상남도 통영시 사량면(蛇梁面)에 있는 산","src\\main\\resources\\static\\mountain_images\\mt_78.jpg"),
        		new Mountain_recommend("천관산","전라남도",724,"봄/가을","2시간30분~3시간미만","하늘을 찌르는 듯한 산","src\\main\\resources\\static\\mountain_images\\mt_79.jpg"),
        		new Mountain_recommend("천마산","경기도",810,"가을/겨울","3시간~3시간30분미만","스키장으로, 청소년 심신수련장으로","src\\main\\resources\\static\\mountain_images\\mt_80.jpg"),
        		new Mountain_recommend("천성산","경상남도",920,"여름","5시간이상","기암괴석이 조각한 금강산의 축소판","src\\main\\resources\\static\\mountain_images\\mt_81.jpg"),
        		new Mountain_recommend("천태산","충청북도",714,"봄/가을","3시간~3시간30분미만","홍건적의 난을 평정한 산","src\\main\\resources\\static\\mountain_images\\mt_82.jpg"),
        		new Mountain_recommend("청량산","경상북도",869,"여름","5시간이상","아름다운 관광자원의 보고","src\\main\\resources\\static\\mountain_images\\mt_83.jpg"),
        		new Mountain_recommend("추월산","전라남도",731,"여름","2시간30분~3시간미만","마르지 않는 약수터를 품은 산","src\\main\\resources\\static\\mountain_images\\mt_84.jpg"),
        		new Mountain_recommend("축령산","경기도",887,"여름/가을","3시간~3시간30분미만","남이장이 심신을 닦던 남이 바위","src\\main\\resources\\static\\mountain_images\\mt_85.jpg"),
        		new Mountain_recommend("치악산","강원도",1282,"여름/가을","5시간이상","골골마다 절의와 은사가 굽이치네","src\\main\\resources\\static\\mountain_images\\mt_86.jpg"),
        		new Mountain_recommend("칠갑산","충청남도",559,"가을","3시간~3시간30분미만","손 대지 않은 자연 그대로의 산","src\\main\\resources\\static\\mountain_images\\mt_87.jpg"),
        		new Mountain_recommend("태백산","강원도",1566,"겨울","5시간이상","단군제 올리는 신성한 산","src\\main\\resources\\static\\mountain_images\\mt_88.jpg"),
        		new Mountain_recommend("태화산","강원도",1027,"여름","4시간30분~5시간미만","태화산성에서 영월읍을 두루 굽어보니","src\\main\\resources\\static\\mountain_images\\mt_89.jpg"),
        		new Mountain_recommend("팔공산","경상북도",1192,"겨울","5시간이상","작은 국보급 문화재 전시장","src\\main\\resources\\static\\mountain_images\\mt_90.jpg"),
        		new Mountain_recommend("팔봉산","강원도",328,"봄/여름","3시간~3시간30분미만","산세와 암릉에 두 번 놀라는 산","src\\main\\resources\\static\\mountain_images\\mt_91.jpg"),
        		new Mountain_recommend("팔영산","전라남도",606,"봄/가을","5시간이상","대마도를 조망하는 천리안","src\\main\\resources\\static\\mountain_images\\mt_92.jpg"),
        		new Mountain_recommend("한라산","제주도",1947,"여름/가을","5시간이상","화산이 잉태한 남한 최고(最高)의 영산","src\\main\\resources\\static\\mountain_images\\mt_93.jpg"),
        		new Mountain_recommend("화악산","경기도",1468,"여름","3시간~3시간30분미만","강원도와 경기도를 가르는 경기도 제 1 고봉","src\\main\\resources\\static\\mountain_images\\mt_94.jpg"),
        		new Mountain_recommend("화왕산","경상남도",757,"봄/가을","2시간~2시간30분미만","봄에는 철쭉 화원, 가을에는 억새꽃밭","src\\main\\resources\\static\\mountain_images\\mt_95.jpg"),
        		new Mountain_recommend("황매산","경상남도",1113,"봄","3시간~3시간30분미만","목장길 따라 가는 세 봉우리","src\\main\\resources\\static\\mountain_images\\mt_96.jpg"),
        		new Mountain_recommend("황석산","경상남도",1192,"가을/겨울","3시간30분~4시간미만","썩은 갈치 다 모이는 오지의 산","src\\main\\resources\\static\\mountain_images\\mt_97.jpg"),
        		new Mountain_recommend("황악산","경상북도",1111,"가을/겨울","3시간~3시간30분미만","학이 먼저 알아 본 국민 관광지","src\\main\\resources\\static\\mountain_images\\mt_98.jpg"),
        		new Mountain_recommend("황장산","경상북도",1078,"여름","3시간~3시간30분미만","옛 대궐 지었던 황장목 주산지","src\\main\\resources\\static\\mountain_images\\mt_99.jpg"),
        		new Mountain_recommend("희양산","경상북도",996,"여름","3시간~3시간30분미만","설악산 울산바위에 필적할 거대한 바위산","src\\main\\resources\\static\\mountain_images\\mt_100.jpg")
        );

        mountainRepository.saveAll(mountains);
    }
}
