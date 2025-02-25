package com.lzx.minecraftparticle.ui.draw.param.widgets;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.ParticleCategory;
import java.util.ArrayList;

public class ParticleCategoryDialogFragment extends DialogFragment{
    ParticleCategoryParamView paramView;
    
    SearchBar searchBar;
    SearchView searchView;
    
    ArrayList<ParticleCategory> particleCategories = new ArrayList<ParticleCategory>();
    
    public ParticleCategoryDialogFragment(ParticleCategoryParamView paramView) {
        this.paramView = paramView;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //加载数据
        initParticleCategories();
        //builder
        View view = getLayoutInflater().inflate(R.layout.dialog_param_particle_category, null);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.dialog_param_particle_category_title);
        builder.setView(view);
        builder.setNegativeButton("关闭", null);
        
        //控件设置
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ParticleCategorySuggestionsAdapter adapter = new ParticleCategorySuggestionsAdapter(this, particleCategories);
        recyclerView.setAdapter(adapter);
        
        searchBar = view.findViewById(R.id.searchBar);
        searchView = view.findViewById(R.id.searchView);
        searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
//            searchBar.setText(searchView.getText());
//            searchView.hide();
            paramView.setSearch(searchView.getText().toString());
            dismiss();
            return false;
        });
        searchView.getEditText().addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
        
        //控件设置
        return builder.create();
    }
    
    //点击搜索项时
    public void onClickSearchSuggestion(String id, String name) {
        //searchBar.setText(s);
        //searchView.hide();
        paramView.setSearch(id);
        Toast.makeText(getContext(), "已选择\"" + name + "\"", Toast.LENGTH_SHORT).show();
        dismiss();
    }
    
    //初始化数据
    public void initParticleCategories() {
        particleCategories.add(new ParticleCategory("minecraft:arrow_spell_emitter", "箭矢粒子"));
        particleCategories.add(new ParticleCategory("minecraft:balloon_gas_particle", "圆形白色粒子"));
        particleCategories.add(new ParticleCategory("minecraft:basic_bubble_particle_manual", "气泡"));
        particleCategories.add(new ParticleCategory("minecraft:basic_crit_particle", "暴击"));
        particleCategories.add(new ParticleCategory("minecraft:basic_flame_particle", "火焰粒子"));
        particleCategories.add(new ParticleCategory("minecraft:basic_smoke_particle", "烟雾粒子"));
        particleCategories.add(new ParticleCategory("minecraft:basic_portal_particle", "传送门"));
        particleCategories.add(new ParticleCategory("minecraft:bleach", "村民愤怒"));
        particleCategories.add(new ParticleCategory("minecraft:block_destruct", "方块破坏粒子"));
        particleCategories.add(new ParticleCategory("minecraft:block_slide", "方块滑"));
        particleCategories.add(new ParticleCategory("minecraft:blue_flame_particle", "灵魂火把蓝色火焰粒子"));
        particleCategories.add(new ParticleCategory("minecraft:breaking_item_icon", "物品破碎粒子"));
        particleCategories.add(new ParticleCategory("minecraft:breaking_item_terrain", "物品破碎粒子"));
        particleCategories.add(new ParticleCategory("minecraft:breeze_ground_particle", "旋风人方块粒子"));
        particleCategories.add(new ParticleCategory("minecraft:breeze_wind_explosion_emitter", "风弹爆炸狂风粒子"));
        particleCategories.add(new ParticleCategory("minecraft:bubble_column_bubble", "白色气泡"));
        particleCategories.add(new ParticleCategory("minecraft:bubble_column_up_particle", "灵魂沙气泡柱"));
        particleCategories.add(new ParticleCategory("minecraft:bubble_column_down_particle", "岩浆块气泡柱"));
        particleCategories.add(new ParticleCategory("minecraft:camera_shoot_explosion", "相机爆炸"));
        particleCategories.add(new ParticleCategory("minecraft:campfire_smoke_particle", "营火烟雾"));
        particleCategories.add(new ParticleCategory("minecraft:campfire_tall_smoke_particle", "干草块营火烟雾"));
        particleCategories.add(new ParticleCategory("minecraft:candle_flame_particle", "蜡烛火焰"));
        particleCategories.add(new ParticleCategory("minecraft:cauldron_bubble_particle", "炼药锅气泡"));
        particleCategories.add(new ParticleCategory("minecraft:cauldron_explosion_emitter", "炼药锅爆炸粒子"));
        particleCategories.add(new ParticleCategory("minecraft:cauldron_spell_emitter", "炼药锅咒语粒子"));
        particleCategories.add(new ParticleCategory("minecraft:cauldron_splash_particle", "炼药锅水花"));
        particleCategories.add(new ParticleCategory("minecraft:cherry_leaves_particle", "樱花飘落"));
        particleCategories.add(new ParticleCategory("minecraft:colored_flame_particle", "彩焰"));
        particleCategories.add(new ParticleCategory("minecraft:conduit_absorb_particle", "潮涌能量"));
        particleCategories.add(new ParticleCategory("minecraft:conduit_attack_emitter", "潮涌核心"));
        particleCategories.add(new ParticleCategory("minecraft:conduit_particle", "潮涌能量"));
        particleCategories.add(new ParticleCategory("minecraft:creaking_crumble_body", "嘎吱身体粒子"));
        particleCategories.add(new ParticleCategory("minecraft:creaking_crumble_head", "嘎吱头粒子"));
        particleCategories.add(new ParticleCategory("minecraft:creaking_heart_trail", "嘎吱之心粒子"));
        particleCategories.add(new ParticleCategory("minecraft:critical_hit_emitter", "大暴击"));
        particleCategories.add(new ParticleCategory("minecraft:crop_growth_emitter", "作物生长"));
        particleCategories.add(new ParticleCategory("minecraft:crop_growth_area_emitter", "作物区域生长"));
        particleCategories.add(new ParticleCategory("minecraft:death_explosion_emitter", "死亡爆炸粒子"));
        particleCategories.add(new ParticleCategory("minecraft:dolphin_move_particle", "海豚游泳"));
        particleCategories.add(new ParticleCategory("minecraft:dragon_breath_fire", "龙息"));
        particleCategories.add(new ParticleCategory("minecraft:dragon_breath_lingering", "龙息"));
        particleCategories.add(new ParticleCategory("minecraft:dragon_breath_trail", "龙息"));
        particleCategories.add(new ParticleCategory("minecraft:dragon_death_explosion_emitter", "末影龙死亡"));
        particleCategories.add(new ParticleCategory("minecraft:dragon_destroy_block", "末影龙破坏方块"));
        particleCategories.add(new ParticleCategory("minecraft:dragon_dying_explosion", "末影龙垂死"));
        particleCategories.add(new ParticleCategory("minecraft:dust_plume", "饰纹陶罐放置物品"));
        particleCategories.add(new ParticleCategory("minecraft:egg_destroy_emitter", "鸡蛋破碎"));
        particleCategories.add(new ParticleCategory("minecraft:electric_spark_particle", "闪电击中铜块"));
        particleCategories.add(new ParticleCategory("minecraft:elephant_tooth_paste_vapor_particle", "白色烟雾"));
        particleCategories.add(new ParticleCategory("minecraft:enchanting_table_particle", "附魔符文"));
        particleCategories.add(new ParticleCategory("minecraft:end_chest", "末影箱"));
        particleCategories.add(new ParticleCategory("minecraft:endrod", "末影烛"));
        particleCategories.add(new ParticleCategory("minecraft:evocation_fang_particle", "尖牙"));
        particleCategories.add(new ParticleCategory("minecraft:evoker_spell", "黑色咒语粒子"));
        particleCategories.add(new ParticleCategory("minecraft:explosion_particle", "小型爆炸"));
        particleCategories.add(new ParticleCategory("minecraft:explosion_manual", "爆炸"));
        particleCategories.add(new ParticleCategory("minecraft:eyeblossom_close", "眼眸花闭合"));
        particleCategories.add(new ParticleCategory("minecraft:eyeblossom_open", "眼眸花开启"));
        particleCategories.add(new ParticleCategory("minecraft:eye_of_ender_bubble_particle", "末影之眼气泡"));
        particleCategories.add(new ParticleCategory("minecraft:eyeofender_death_explode_particle", "末影之眼破碎"));
        particleCategories.add(new ParticleCategory("minecraft:falling_border_dust_particle", "下落的治疗效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:falling_dust_concrete_particle", "下落的混凝土"));
        particleCategories.add(new ParticleCategory("minecraft:falling_dust_dragon_egg_particle", "下落的龙蛋"));
        particleCategories.add(new ParticleCategory("minecraft:falling_dust_gravel_particle", "下落的沙砾"));
        particleCategories.add(new ParticleCategory("minecraft:falling_dust_red_sand_particle", "下落的红沙"));
        particleCategories.add(new ParticleCategory("minecraft:falling_dust_sand_particle", "下落的沙子"));
        particleCategories.add(new ParticleCategory("minecraft:falling_dust_scaffolding_particle", "下落的脚手架"));
        particleCategories.add(new ParticleCategory("minecraft:falling_dust_top_snow_particle", "下落的雪"));
        particleCategories.add(new ParticleCategory("minecraft:fish_hook_particle", "鱼钩粒子"));
        particleCategories.add(new ParticleCategory("minecraft:fish_pos_particle", "钓鱼气泡"));
        //particleCategories.add(new ParticleCategory("minecraft:flash", "烟花火箭爆炸"));
        particleCategories.add(new ParticleCategory("minecraft:glow_particle", "发光粒子"));
        particleCategories.add(new ParticleCategory("minecraft:guardian_attack_particle", "守卫者攻击"));
        particleCategories.add(new ParticleCategory("minecraft:guardian_water_move_particle", "守卫者移动"));
        particleCategories.add(new ParticleCategory("minecraft:heart_particle", "生物驯服繁殖"));
        particleCategories.add(new ParticleCategory("minecraft:honey_drip_particle", "蜂蜜粒子"));
        particleCategories.add(new ParticleCategory("minecraft:huge_explosion_emitter", "大型爆炸"));
        particleCategories.add(new ParticleCategory("minecraft:huge_explosion_lab_misc_emitter", "大型爆炸"));
        particleCategories.add(new ParticleCategory("minecraft:ice_evaporation_emitter", "冰蒸发"));
        particleCategories.add(new ParticleCategory("minecraft:infested_ambient", "寄生粒子"));
        particleCategories.add(new ParticleCategory("minecraft:infested_emitter", "寄生粒子"));
        particleCategories.add(new ParticleCategory("minecraft:ink_emitter", "墨水"));
        particleCategories.add(new ParticleCategory("minecraft:knockback_roar_particle", "击退粒子"));
        particleCategories.add(new ParticleCategory("minecraft:lab_table_heatblock_dust_particle", "元素构造器粒子"));
        particleCategories.add(new ParticleCategory("minecraft:lab_table_misc_mystical_particle", "元素构造器粒子"));
        particleCategories.add(new ParticleCategory("minecraft:large_explosion", "爆炸"));
        particleCategories.add(new ParticleCategory("minecraft:lava_particle", "岩浆粒子"));
        particleCategories.add(new ParticleCategory("minecraft:lava_drip_particle", "渗透岩浆粒子"));
        particleCategories.add(new ParticleCategory("minecraft:llama_spit_smoke", "羊驼口水"));
        particleCategories.add(new ParticleCategory("minecraft:magnesium_salts_emitter", "白色粒子"));
        particleCategories.add(new ParticleCategory("minecraft:misc_fire_vapor_particle", "蒸汽粒子"));
        particleCategories.add(new ParticleCategory("minecraft:mob_block_spawn_emitter", "怪物砖激活"));
        particleCategories.add(new ParticleCategory("minecraft:mobflame_emitter", "实体着火"));
        particleCategories.add(new ParticleCategory("minecraft:mobflame_single", "实体着火"));
        particleCategories.add(new ParticleCategory("minecraft:mob_portal", "末影粒子"));
        particleCategories.add(new ParticleCategory("minecraft:mobspell_ambient", "生物咒语效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:mobspell_emitter", "生物咒语效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:mobspell_lingering", "生物咒语效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:mycelium_dust_particle", "菌丝体孢子粒子"));
        particleCategories.add(new ParticleCategory("minecraft:nectar_drip_particle", "蜜蜂掉落的花粉"));
        particleCategories.add(new ParticleCategory("minecraft:note_particle", "音符"));
        particleCategories.add(new ParticleCategory("minecraft:obsidian_glow_dust_particle", "黑曜石粒子"));
        particleCategories.add(new ParticleCategory("minecraft:obsidian_tear_particle", "哭泣的黑曜石"));
        particleCategories.add(new ParticleCategory("minecraft:ominous_spawning_particle", "不详之物生成器粒子"));
        particleCategories.add(new ParticleCategory("minecraft:oozing_ambient", "渗透效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:oozing_emitter", "渗透效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:pale_oak_leaves_particle", "苍白橡树树叶"));
        particleCategories.add(new ParticleCategory("minecraft:phantom_trail_particle", "幻翼轨迹"));
        particleCategories.add(new ParticleCategory("minecraft:portal_directional", "传送门"));
        particleCategories.add(new ParticleCategory("minecraft:portal_east_west", "传送门"));
        particleCategories.add(new ParticleCategory("minecraft:portal_north_south", "传送门"));
        particleCategories.add(new ParticleCategory("minecraft:portal_reverse_particle", "重生锚"));
        particleCategories.add(new ParticleCategory("minecraft:raid_omen_ambient", "袭击之兆效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:raid_omen_emitter", "袭击之兆效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:rain_splash_particle", "雨滴"));
        particleCategories.add(new ParticleCategory("minecraft:redstone_ore_dust_particle", "红石矿石粒子"));
        particleCategories.add(new ParticleCategory("minecraft:redstone_repeater_dust_particle", "红石中继器粒子"));
        particleCategories.add(new ParticleCategory("minecraft:redstone_wire_dust_particle", "充能红石粒子"));
        particleCategories.add(new ParticleCategory("minecraft:rising_border_dust_particle", "升起的红石粒子"));
        particleCategories.add(new ParticleCategory("minecraft:sculk_charge_particle", "幽匿信号蔓延"));
        particleCategories.add(new ParticleCategory("minecraft:sculk_charge_pop_particle", "幽匿信号丢失"));
        particleCategories.add(new ParticleCategory("minecraft:sculk_sensor_redstone_particle", "激活的幽匿感测体"));
        particleCategories.add(new ParticleCategory("minecraft:sculk_soul_particle", "幽匿催发体探测到生物死亡"));
        particleCategories.add(new ParticleCategory("minecraft:shriek_particle", "幽匿尖啸体激活"));
        particleCategories.add(new ParticleCategory("minecraft:shulker_bullet", "头颅粒子"));
        particleCategories.add(new ParticleCategory("minecraft:silverfish_grief_emitter", "白色烟雾"));
        particleCategories.add(new ParticleCategory("minecraft:small_flame_particle", "小型火焰"));
        particleCategories.add(new ParticleCategory("minecraft:small_soul_fire_flame", "小型灵魂火焰"));
        particleCategories.add(new ParticleCategory("minecraft:smash_ground_particle", "撞击地面"));
        particleCategories.add(new ParticleCategory("minecraft:smash_ground_particle_center", "撞击地面中心"));
        particleCategories.add(new ParticleCategory("minecraft:sneeze", "打喷嚏"));
        particleCategories.add(new ParticleCategory("minecraft:snowflake_particle", "细雪行走粒子"));
        particleCategories.add(new ParticleCategory("minecraft:sonic_explosion", "音波"));
        particleCategories.add(new ParticleCategory("minecraft:soul_particle", "灵魂沙行走粒子"));
        particleCategories.add(new ParticleCategory("minecraft:sparkler_emitter", "烟花尾迹"));
        particleCategories.add(new ParticleCategory("minecraft:splash_spell_emitter", "法术粒子"));
        particleCategories.add(new ParticleCategory("minecraft:sponge_absorb_water_particle", "气泡"));
        particleCategories.add(new ParticleCategory("minecraft:spore_blossom_ambient_particle", "孢子花发散的孢子"));
        particleCategories.add(new ParticleCategory("minecraft:spore_blossom_shower_particle", "孢子花掉落的孢子"));
        particleCategories.add(new ParticleCategory("minecraft:squid_flee_particle", "气泡"));
        particleCategories.add(new ParticleCategory("minecraft:squid_ink_bubble", "小型蓝色气泡"));
        particleCategories.add(new ParticleCategory("minecraft:squid_move_particle", "蓝色气泡"));
        particleCategories.add(new ParticleCategory("minecraft:stalactite_lava_drip_particle", "钟乳石岩浆滴落"));
        particleCategories.add(new ParticleCategory("minecraft:stalactite_water_drip_particle", "钟乳石水滴落"));
        particleCategories.add(new ParticleCategory("minecraft:stunned_emitter", "眩晕"));
        particleCategories.add(new ParticleCategory("minecraft:totem_manual", "不死图腾"));
        particleCategories.add(new ParticleCategory("minecraft:totem_particle", "不死图腾"));
        particleCategories.add(new ParticleCategory("minecraft:trial_omen_ambient", "试炼之兆效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:trial_omen_emitter", "试炼之兆效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:trial_omen_single", "试炼之兆效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:trial_spawner_detection", "试炼刷怪笼粒子"));
        particleCategories.add(new ParticleCategory("minecraft:trial_spawner_detection_ominous", "不详试炼刷怪笼粒子"));
        particleCategories.add(new ParticleCategory("minecraft:underwater_torch_particle", "水下火把"));
        particleCategories.add(new ParticleCategory("minecraft:vault_connection_particle", "宝库连接粒子"));
        particleCategories.add(new ParticleCategory("minecraft:vibration_signal", "振动粒子"));
        particleCategories.add(new ParticleCategory("minecraft:villager_angry", "村民生气"));
        particleCategories.add(new ParticleCategory("minecraft:villager_happy", "村民开心"));
        particleCategories.add(new ParticleCategory("minecraft:warden_dig", "监守者钻地"));
        particleCategories.add(new ParticleCategory("minecraft:water_drip_particle", "渗透水粒子"));
        particleCategories.add(new ParticleCategory("minecraft:water_evaporation_actor_emitter", "着火进入水中"));
        particleCategories.add(new ParticleCategory("minecraft:water_evaporation_bucket_emitter", "着火进入水中"));
        particleCategories.add(new ParticleCategory("minecraft:water_evaporation_manual", "着火进入水中"));
        particleCategories.add(new ParticleCategory("minecraft:water_splash_particle", "水花"));
        particleCategories.add(new ParticleCategory("minecraft:water_splash_particle_manual", "水花"));
        particleCategories.add(new ParticleCategory("minecraft:water_wake_particle", "向上的水粒子"));
        particleCategories.add(new ParticleCategory("minecraft:wax_particle", "打蜡除蜡粒子"));
        particleCategories.add(new ParticleCategory("minecraft:weaving_ambient", "编织效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:weaving_emitter", "编织效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:white_smoke_particle", "白烟粒子"));
        particleCategories.add(new ParticleCategory("minecraft:wind_charged_ambient", "蓄风效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:wind_charged_emitter", "蓄风效果粒子"));
        particleCategories.add(new ParticleCategory("minecraft:wind_explosion_emitter", "风弹爆炸"));
        particleCategories.add(new ParticleCategory("minecraft:witchspell_emitter", "女巫"));
        particleCategories.add(new ParticleCategory("minecraft:wither_boss_invulnerable", "凋灵粒子"));
    }
}
